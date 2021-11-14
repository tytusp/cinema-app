package com.tytuspawlak.cinema.core.dto.movie;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
@Value
public class MovieDTO {
    String id;
    String title;
    String imdbId;
}
