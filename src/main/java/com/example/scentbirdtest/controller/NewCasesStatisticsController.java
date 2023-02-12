package com.example.scentbirdtest.controller;

import com.example.scentbirdtest.model.NewCasesStatisticsResponse;
import com.example.scentbirdtest.service.NewCasesStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Set;

@RestController
public class NewCasesStatisticsController {

    @Autowired
    NewCasesStatisticsService service;

    @GetMapping("/statistics")
    public NewCasesStatisticsResponse getStatistic(@RequestParam Set<String> country, LocalDate from, LocalDate to) {
        return service.getStatistics(country, from, to);
    }
}
