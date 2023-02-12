package com.example.scentbirdtest.client;

import com.example.scentbirdtest.client.model.DailyCases;

import java.time.LocalDate;
import java.util.List;

public interface CovidClient {

    List<DailyCases> getDailyCasesList(String country, LocalDate dateFrom, LocalDate dateTo);
}
