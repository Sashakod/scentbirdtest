package com.example.scentbirdtest.client;

import com.example.scentbirdtest.client.model.DailyCases;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@Primary
public class CovidClientCachedImpl implements CovidClient {
    private final CovidClient covidClient;
    private final CacheManager cacheManager;

    public CovidClientCachedImpl(@Qualifier("covidClientImpl") CovidClient covidClient, CacheManager cacheManager) {
        this.covidClient = covidClient;
        this.cacheManager = cacheManager;
    }

    @Override
    @Cacheable("dailyCases")
    public List<DailyCases> getDailyCasesList(String country, LocalDate dateFrom, LocalDate dateTo) {
        return covidClient.getDailyCasesList(country, dateFrom, dateTo);
    }

    @Scheduled(fixedRate = 3600000)
    public void evictDailyCasesCache() {
        Objects.requireNonNull(cacheManager.getCache("dailyCases")).clear();
    }
}
