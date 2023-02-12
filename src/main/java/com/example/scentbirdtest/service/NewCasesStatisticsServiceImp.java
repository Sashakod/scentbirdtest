package com.example.scentbirdtest.service;

import com.example.scentbirdtest.client.CovidClient;
import com.example.scentbirdtest.client.model.DailyCases;
import com.example.scentbirdtest.model.CasesCount;
import com.example.scentbirdtest.model.DailyNewCases;
import com.example.scentbirdtest.model.NewCasesStatistics;
import com.example.scentbirdtest.model.NewCasesStatisticsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class NewCasesStatisticsServiceImp implements NewCasesStatisticsService {

    private final CovidClient covidClient;

    @Override
    public NewCasesStatisticsResponse getStatistics(Set<String> countries, LocalDate dateFrom, LocalDate dateTo) {
        final List<CompletableFuture<NewCasesStatistics>> statisticsFutures = countries.stream()
                .map(country -> CompletableFuture.supplyAsync(() -> covidClient.getDailyCasesList(country,
                                dateFrom.minusDays(1), dateTo))
                        .thenApplyAsync(this::calculateNewCases)
                        .thenApplyAsync(this::findMinAndMaxValues))
                .toList();

        CompletableFuture<Set<NewCasesStatistics>> statisticsSetFuture = joinAll(statisticsFutures);

        try {
            return new NewCasesStatisticsResponse(statisticsSetFuture.get(10, TimeUnit.SECONDS), dateFrom, dateTo);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    private List<DailyNewCases> calculateNewCases(List<DailyCases> dailyCasesList) {
        List<DailyNewCases> dailyNewCasesList = new ArrayList<>(dailyCasesList.size() - 1);

        for (int i = 1; i < dailyCasesList.size(); i++) {
            DailyCases currentDay = dailyCasesList.get(i);
            DailyCases previousDay = dailyCasesList.get(i - 1);

            dailyNewCasesList.add(new DailyNewCases(currentDay.country(),
                    currentDay.casesCount() - previousDay.casesCount(), currentDay.date().toLocalDate()));
        }

        return dailyNewCasesList;
    }

    private NewCasesStatistics findMinAndMaxValues(List<DailyNewCases> countryDailyNewCases) {
        CasesCount minCases = countryDailyNewCases.stream()
                .min(Comparator.comparing(DailyNewCases::casesCount))
                .map(minDailyNewCases -> new CasesCount(minDailyNewCases.casesCount(), minDailyNewCases.date()))
                .orElseThrow(() -> new IllegalArgumentException("There is no data of new casesCount"));

        CasesCount maxCases = countryDailyNewCases.stream()
                .max(Comparator.comparing(DailyNewCases::casesCount))
                .map(maxDailyNewCases -> new CasesCount(maxDailyNewCases.casesCount(), maxDailyNewCases.date()))
                .orElseThrow(() -> new IllegalArgumentException("There is no data of new casesCount"));

        String country = countryDailyNewCases.stream()
                .findAny()
                .map(DailyNewCases::country)
                .orElseThrow(() -> new IllegalArgumentException("There is no data of new casesCount"));

        return new NewCasesStatistics(country, minCases, maxCases);
    }

    private CompletableFuture<Set<NewCasesStatistics>> joinAll(List<CompletableFuture<NewCasesStatistics>> futures) {
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toSet()));
    }
}
