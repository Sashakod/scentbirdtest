package com.example.scentbirdtest.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;

public record DailyCases(@JsonProperty("Country") String country,
                         @JsonProperty("Cases") int casesCount,
                         @JsonProperty("Date") ZonedDateTime date) {

}
