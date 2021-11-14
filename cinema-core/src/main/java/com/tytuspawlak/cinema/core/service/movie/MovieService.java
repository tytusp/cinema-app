package com.tytuspawlak.cinema.core.service.movie;

import com.tytuspawlak.cinema.core.dto.movie.MovieDTO;
import com.tytuspawlak.cinema.core.dto.movie.MovieSearchFilter;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MovieService {
    List<MovieDTO> listAllMovies();

    List<MovieDTO> addMovies(Collection<MovieDTO> movies);

    List<MovieDTO> searchMovies(MovieSearchFilter searchFilter);

    Optional<MovieDTO> findMovieById(String id);

    Optional<MovieDTO> findMovieByImdbId(String imdbId);
}
