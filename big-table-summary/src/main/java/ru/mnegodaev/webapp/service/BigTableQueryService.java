package ru.mnegodaev.webapp.service;

import java.util.List;

public interface BigTableQueryService {

    List<Long> performQueryForInterval(long from, long to);

    List<Long> performQueryInParallel(int dop);
}
