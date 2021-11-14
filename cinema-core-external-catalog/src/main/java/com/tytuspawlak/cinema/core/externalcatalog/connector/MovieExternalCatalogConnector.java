package com.tytuspawlak.cinema.core.externalcatalog.connector;

import com.tytuspawlak.cinema.core.dto.movie.MovieDetailsDTO;

import java.util.Optional;

public interface MovieExternalCatalogConnector {
    Optional<MovieDetailsDTO> findMovieDetails(String id);
}
