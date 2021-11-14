package com.tytuspawlak.cinema.core.externalcatalog.connector.omdb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
@Value
public class OMDbRatingTO {
    @JsonProperty("Source")
    String source;
    @JsonProperty("Value")
    String value;
}
