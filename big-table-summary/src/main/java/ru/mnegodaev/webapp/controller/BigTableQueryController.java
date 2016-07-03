package ru.mnegodaev.webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.mnegodaev.webapp.model.ResponseMessage;
import ru.mnegodaev.webapp.service.BigTableQueryService;

import java.util.Arrays;
import java.util.List;

@RestController()
public class BigTableQueryController {
    @Autowired
    BigTableQueryService service;

    //TODO Заменить асинхронным вызовом с возможностью проверки статуса запроса и получения результата
    @RequestMapping("/query")
    public ResponseMessage greeting(@RequestParam(value="dop", defaultValue="4") int dop) {
        List<Long> summary = service.performQueryInParallel(dop);
        return new ResponseMessage("result", Arrays.toString(summary.toArray()));
    }
}
