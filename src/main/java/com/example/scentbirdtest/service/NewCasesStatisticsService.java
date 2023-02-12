package com.example.scentbirdtest.service;

import com.example.scentbirdtest.model.NewCasesStatisticsResponse;

import java.time.LocalDate;
import java.util.Set;

public interface NewCasesStatisticsService {

    NewCasesStatisticsResponse getStatistics(Set<String> countries, LocalDate dateFrom, LocalDate dateTo);
}
