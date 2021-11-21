package com.meli.ipapi.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public class ExchangeRate {
    public String base;
    @JsonProperty("rates")
    public JsonNode rates;
}
