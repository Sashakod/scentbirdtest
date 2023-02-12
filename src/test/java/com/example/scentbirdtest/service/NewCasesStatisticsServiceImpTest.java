package com.example.scentbirdtest.service;

import com.example.scentbirdtest.client.CovidClient;
import com.example.scentbirdtest.client.model.DailyCases;
import com.example.scentbirdtest.model.CasesCount;
import com.example.scentbirdtest.model.NewCasesStatistics;
import com.example.scentbirdtest.model.NewCasesStatisticsResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NewCasesStatisticsServiceImpTest {

    @InjectMocks
    private NewCasesStatisticsServiceImp statisticService;

    @Mock
    private CovidClient covidClient;

    @Test
    void shouldGetStatisticsForCountries() {
        //given
        NewCasesStatisticsResponse expectedStatistic = new NewCasesStatisticsResponse(
                Set.of(
                        new NewCasesStatistics("Mexico",
                                new CasesCount(0, LocalDate.parse("2023-01-01")),
                                new CasesCount(18477, LocalDate.parse("2023-01-03"))),
                        new NewCasesStatistics("Argentina",
                                new CasesCount(0, LocalDate.parse("2023-01-01")),
                                new CasesCount(72558, LocalDate.parse("2023-01-02")))
                ),
                LocalDate.parse("2023-01-01"),
                LocalDate.parse("2023-01-03")
        );

        Set<String> countries = Set.of("mexico", "argentina");
        LocalDate dateFrom = LocalDate.parse("2023-01-01");
        LocalDate dateTo = LocalDate.parse("2023-01-03");

        when(covidClient.getDailyCasesList("mexico", dateFrom.minusDays(1), dateTo)).thenReturn(List.of(
                        new DailyCases("Mexico", 7234467, ZonedDateTime.parse("2022-12-31T00:00:00Z")),
                        new DailyCases("Mexico", 7234467, ZonedDateTime.parse("2023-01-01T00:00:00Z")),
                        new DailyCases("Mexico", 7234467, ZonedDateTime.parse("2023-01-02T00:00:00Z")),
                        new DailyCases("Mexico", 7252944, ZonedDateTime.parse("2023-01-03T00:00:00Z"))
                )
        );

        when(covidClient.getDailyCasesList("argentina", dateFrom.minusDays(1), dateTo)).thenReturn(List.of(
                        new DailyCases("Argentina", 9891139, ZonedDateTime.parse("2022-12-31T00:00:00Z")),
                        new DailyCases("Argentina", 9891139, ZonedDateTime.parse("2023-01-01T00:00:00Z")),
                        new DailyCases("Argentina", 9963697, ZonedDateTime.parse("2023-01-02T00:00:00Z")),
                        new DailyCases("Argentina", 9963697, ZonedDateTime.parse("2023-01-03T00:00:00Z"))
                )
        );

        //when
        NewCasesStatisticsResponse statistic = statisticService.getStatistics(countries, dateFrom, dateTo);

        //then
        assertEquals(expectedStatistic, statistic);
        verify(covidClient).getDailyCasesList("mexico", LocalDate.parse("2022-12-31"),
                LocalDate.parse("2023-01-03"));
        verify(covidClient).getDailyCasesList("argentina", LocalDate.parse("2022-12-31"),
                LocalDate.parse("2023-01-03"));
        verifyNoMoreInteractions(covidClient);
    }
}