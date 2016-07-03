package ru.mnegodaev.webapp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class QueryRunner implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(QueryRunner.class);
    private static final String QUERY = "SELECT " +
            "SUM(col0), SUM(col1), SUM(col2), SUM(col3), SUM(col4), SUM(col5), SUM(col6), SUM(col7), SUM(col8), SUM(col9)," +
            "SUM(col10), SUM(col11), SUM(col12), SUM(col13), SUM(col14), SUM(col15), SUM(col16), SUM(col17), SUM(col18), SUM(col19)," +
            "SUM(col20), SUM(col21), SUM(col22), SUM(col23), SUM(col24), SUM(col25), SUM(col26), SUM(col27), SUM(col28), SUM(col29)," +
            "SUM(col30), SUM(col31), SUM(col32), SUM(col33), SUM(col34), SUM(col35), SUM(col36), SUM(col37), SUM(col38), SUM(col39)," +
            "SUM(col40), SUM(col41), SUM(col42), SUM(col43), SUM(col44), SUM(col45), SUM(col46), SUM(col47), SUM(col48), SUM(col49)," +
            "SUM(col50), SUM(col51), SUM(col52), SUM(col53), SUM(col54), SUM(col55), SUM(col56), SUM(col57), SUM(col58), SUM(col59)," +
            "SUM(col60), SUM(col61), SUM(col62), SUM(col63), SUM(col64), SUM(col65), SUM(col66), SUM(col67), SUM(col68), SUM(col69)," +
            "SUM(col70), SUM(col71), SUM(col72), SUM(col73), SUM(col74), SUM(col75), SUM(col76), SUM(col77), SUM(col78), SUM(col79)," +
            "SUM(col80), SUM(col81), SUM(col82), SUM(col83), SUM(col84), SUM(col85), SUM(col86), SUM(col87), SUM(col88), SUM(col89)," +
            "SUM(col90), SUM(col91), SUM(col92), SUM(col93), SUM(col94), SUM(col95), SUM(col96), SUM(col97), SUM(col98), SUM(col99)" +
            " FROM big_table WHERE id BETWEEN ?1 AND ?2";
    private static final int NUMBER_OF_COLUMNS = 100;

    private JdbcTemplate jdbcTemplate;
    private long from;
    private long to;
    private List<Long> queryResult;

    public QueryRunner(JdbcTemplate jdbcTemplate, long from, long to) {
        this.from = from;
        this.to = to;
        this.jdbcTemplate = jdbcTemplate;

    }

    public void run() {
        long startTime = System.currentTimeMillis();

        queryResult = jdbcTemplate.query(QUERY, new Long[]{from, to}, (resultSet, i) -> {
            List<Long> summary = new ArrayList<>();
            for (int colNum = 1; colNum <= NUMBER_OF_COLUMNS; colNum++) {
                summary.add(resultSet.getLong(colNum));
            }
            return summary;
        }).get(0); // запрос возвращает ровно одну строку

        logger.debug(String.format("Execution time for interval %s..%s: %d ms", from, to,
                System.currentTimeMillis() - startTime));
    }

    public List<Long> getQueryResult() {
        return queryResult;
    }
}
