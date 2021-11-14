package com.tytuspawlak.cinema.core.dto.movie;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class MovieDetailsDTO {
    String title;
    String year;
    String rated;
    String released;
    String runtime;
    String genre;
    String director;
    String writer;
    String actors;
    String plot;
    String language;
    String country;
    String awards;
    String poster;
    String metascore;
    String imdbRating;
    String imdbVotes;
    String imdbID;
    String type;
    String dvd;
    String boxOffice;
    String production;
    String website;
    List<MovieDetailsRatingDTO> ratings;
}
