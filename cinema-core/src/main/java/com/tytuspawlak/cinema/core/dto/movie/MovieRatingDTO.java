package com.tytuspawlak.cinema.core.dto.movie;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;

@Builder(toBuilder = true)
@Jacksonized
@Value
public class MovieRatingDTO {
    String id;
    String movieId;
    Integer stars;
    String user;
    String review;
    LocalDateTime createDate;
}
