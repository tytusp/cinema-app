package com.tytuspawlak.cinema.core.service.movie;

import com.tytuspawlak.cinema.core.dto.movie.MovieRatingDTO;

import java.util.Collection;
import java.util.List;

public interface MovieRatingService {
    List<MovieRatingDTO> addMovieRatings(Collection<MovieRatingDTO> ratings);
}
