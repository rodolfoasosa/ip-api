package com.meli.ipapi.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public class Country {
    @JsonProperty("currencies")
    public JsonNode currencies;
}
