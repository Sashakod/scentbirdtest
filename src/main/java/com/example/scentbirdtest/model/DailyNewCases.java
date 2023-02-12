package com.example.scentbirdtest.model;

import java.time.LocalDate;

public record DailyNewCases(String country, int casesCount, LocalDate date) {
}
