package ru.mnegodaev.webapp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class BigTableQueryServiceImpl implements BigTableQueryService {

    private static final Logger logger = LoggerFactory.getLogger(BigTableQueryServiceImpl.class);
    private static final String SELECT_MIN_ID_QUERY = "SELECT MIN (id) FROM big_table";
    private static final String SELECT_MAX_ID_QUERY = "SELECT MAX (id) FROM big_table";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Long> performQueryForInterval(long from, long to) {
        QueryRunner runner = new QueryRunner(jdbcTemplate, from, to);
        runner.run();
        return runner.getQueryResult();
    }

    @Override
    public List<Long> performQueryInParallel(int dop) {
        long startTime = System.currentTimeMillis();
        Long firstId = jdbcTemplate.queryForObject(SELECT_MIN_ID_QUERY, Long.class);
        Long lastId = jdbcTemplate.queryForObject(SELECT_MAX_ID_QUERY, Long.class);
        Long interval = ((lastId - firstId) / dop);

        List<QueryRunner> runners = new ArrayList<>(dop);
        ExecutorService threadPool = Executors.newCachedThreadPool();
        for (long id = firstId; id <= lastId; id += interval + 1) {
            QueryRunner runner = new QueryRunner(jdbcTemplate, id, id + interval);
            runners.add(runner);
            threadPool.execute(runner);
        }

        waitUntilFinished(threadPool);
        List<Long> summary = buildTotalSummary(runners);
        logger.debug(String.format("Execution time for total: %d ms", System.currentTimeMillis() - startTime));

        return summary;
    }

    private void waitUntilFinished(ExecutorService threadPool) {
        threadPool.shutdown();

        try {
            threadPool.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            logger.error("Awaiting termination of the threads has been interrupted", e);
        }
    }

    private List<Long> buildTotalSummary(List<QueryRunner> runners) {
        List<Long> totalSummary = null;
        for (QueryRunner runner : runners) {
            if (runner.getQueryResult() == null) {
                continue;
            }

            if (totalSummary == null) {
                totalSummary = runner.getQueryResult();
            } else {
                for (int i = 0; i < totalSummary.size(); i++) {
                    Long sum = totalSummary.get(i) + runner.getQueryResult().get(i);
                    totalSummary.set(i, sum);
                }
            }
        }

        return totalSummary;
    }
}
