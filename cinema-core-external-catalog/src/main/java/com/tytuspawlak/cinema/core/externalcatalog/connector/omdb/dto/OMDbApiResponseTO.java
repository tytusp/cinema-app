package com.tytuspawlak.cinema.core.externalcatalog.connector.omdb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Builder
@Jacksonized
@Value
public class OMDbApiResponseTO {
    @JsonProperty("Title")
    String title;
    @JsonProperty("Year")
    String year;
    @JsonProperty("Rated")
    String rated;
    @JsonProperty("Released")
    String released;
    @JsonProperty("Runtime")
    String runtime;
    @JsonProperty("Genre")
    String genre;
    @JsonProperty("Director")
    String director;
    @JsonProperty("Writer")
    String writer;
    @JsonProperty("Actors")
    String actors;
    @JsonProperty("Plot")
    String plot;
    @JsonProperty("Language")
    String language;
    @JsonProperty("Country")
    String country;
    @JsonProperty("Awards")
    String awards;
    @JsonProperty("Poster")
    String poster;
    @JsonProperty("Metascore")
    String metascore;
    String imdbRating;
    String imdbVotes;
    String imdbID;
    @JsonProperty("Type")
    String type;
    @JsonProperty("DVD")
    String dvd;
    @JsonProperty("BoxOffice")
    String boxOffice;
    @JsonProperty("Production")
    String production;
    @JsonProperty("Website")
    String website;
    @JsonProperty("Ratings")
    List<OMDbRatingTO> ratings;
}
