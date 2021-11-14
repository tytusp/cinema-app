package com.tytuspawlak.cinema.core.service.movie;

import com.tytuspawlak.cinema.core.dto.movie.MovieDetailsDTO;
import com.tytuspawlak.cinema.core.dto.movie.MovieExternalCatalogType;

import java.util.Optional;

public interface MovieDetailsService {
    Optional<MovieDetailsDTO> findMovieDetails(String id, MovieExternalCatalogType catalogType);
}
