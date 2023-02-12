package com.example.scentbirdtest.client;

import com.example.scentbirdtest.client.model.DailyCases;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CovidClientImpl implements CovidClient {

    private final static String CONFIRMED_CASES_BY_COUNTRY_URL = "https://api.covid19api.com/country/{country}/status/confirmed?from={dateFrom}&to={dateTo}";

    private final RestTemplate restTemplate;

    @Override
    @SuppressWarnings("unchecked")
    public List<DailyCases> getDailyCasesList(String country, LocalDate dateFrom, LocalDate dateTo) {
        return Optional.ofNullable(restTemplate.exchange(
                                CONFIRMED_CASES_BY_COUNTRY_URL,
                                HttpMethod.GET,
                                null,
                                new ParameterizedTypeReference<List<DailyCases>>() {
                                },
                                country,
                                ZonedDateTime.of(dateFrom, LocalTime.MIN, ZoneId.of("UTC")),
                                ZonedDateTime.of(dateTo, LocalTime.MIN, ZoneId.of("UTC")))
                        .getBody())
                .orElse(Collections.EMPTY_LIST);
    }
}
