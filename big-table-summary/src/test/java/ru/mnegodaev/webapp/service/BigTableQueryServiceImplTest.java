package ru.mnegodaev.webapp.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.mnegodaev.webapp.context.TestApplicationContext;
import ru.mnegodaev.webapp.context.TestPersistenceContext;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestApplicationContext.class, TestPersistenceContext.class})
@TestPropertySource(locations="classpath:application-test.properties")
public class BigTableQueryServiceImplTest {

    @Autowired
    private BigTableQueryService service;

    @Test
    public void testSubmitInParallel() {
        service.performQueryForInterval(1L, 100000L); // первый запрос для разогрева БД

        List<Long> summary1 = service.performQueryInParallel(1);
        List<Long> summary2 = service.performQueryInParallel(4);
        Assert.assertArrayEquals(summary1.toArray(), summary2.toArray());
    }

    @Test
    public void testQueryResults() {
        List<Long> summary = service.performQueryForInterval(1L, 2L);
        Assert.assertNotNull(summary);
        Assert.assertEquals(Long.valueOf(0L), summary.get(0));
        Assert.assertEquals(Long.valueOf(2L), summary.get(1));
        Assert.assertEquals(Long.valueOf(4L), summary.get(2));
        Assert.assertEquals(Long.valueOf(6L), summary.get(3));
        Assert.assertEquals(Long.valueOf(18L), summary.get(9));
        Assert.assertEquals(Long.valueOf(0L), summary.get(90));
        Assert.assertEquals(Long.valueOf(2L), summary.get(91));
        Assert.assertEquals(Long.valueOf(4L), summary.get(92));
        Assert.assertEquals(Long.valueOf(6L), summary.get(93));
        Assert.assertEquals(Long.valueOf(18L), summary.get(99));

        summary = service.performQueryForInterval(3L, 5L);
        Assert.assertEquals(Long.valueOf(0L), summary.get(0));
        Assert.assertEquals(Long.valueOf(3L), summary.get(1));
        Assert.assertEquals(Long.valueOf(6L), summary.get(2));
        Assert.assertEquals(Long.valueOf(9L), summary.get(3));
        Assert.assertEquals(Long.valueOf(27L), summary.get(9));
        Assert.assertEquals(Long.valueOf(0L), summary.get(90));
        Assert.assertEquals(Long.valueOf(3L), summary.get(91));
        Assert.assertEquals(Long.valueOf(6L), summary.get(92));
        Assert.assertEquals(Long.valueOf(9L), summary.get(93));
        Assert.assertEquals(Long.valueOf(27L), summary.get(99));
    }
}