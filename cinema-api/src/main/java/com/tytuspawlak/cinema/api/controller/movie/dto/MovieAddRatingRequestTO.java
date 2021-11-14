package com.tytuspawlak.cinema.api.controller.movie.dto;

import com.tytuspawlak.cinema.core.dto.movie.MovieIdType;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Value
public class MovieAddRatingRequestTO {
    MovieIdType idType;
    MovieRatingToAddTO rating;
}
