package com.example.scentbirdtest.model;

import java.time.LocalDate;
import java.util.Set;

public record NewCasesStatisticsResponse(Set<NewCasesStatistics> newCasesStatistics, LocalDate from, LocalDate to) {
}
