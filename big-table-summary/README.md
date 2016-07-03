Executing a big table fullscan SQL query in parallel
==================================================

This library allows to execute a single fullscan SQL SELECT query in parallel. The DOP (Degree of parallelism) value is accepted as an input parameter of the submit query method.

Usage
-------------

public class MyClass {

    private static final int dop = 4;

    @Autowired
    private BigTableQueryService service;

    @Test
    public void testSubmitInParallel() {
        List<Long> queryResponce = service.performQueryInParallel(dop);
    }
}