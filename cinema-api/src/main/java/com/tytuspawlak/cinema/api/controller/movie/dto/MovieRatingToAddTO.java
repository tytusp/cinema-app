package com.tytuspawlak.cinema.api.controller.movie.dto;

import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Value
public class MovieRatingToAddTO {
    Integer stars;
    String user;
    String review;
}
