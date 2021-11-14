package com.tytuspawlak.cinema.core.externalcatalog.service;

import com.tytuspawlak.cinema.core.dto.movie.MovieDetailsDTO;
import com.tytuspawlak.cinema.core.dto.movie.MovieExternalCatalogType;
import com.tytuspawlak.cinema.core.externalcatalog.connector.MovieExternalCatalogConnector;
import com.tytuspawlak.cinema.core.externalcatalog.connector.MovieExternalCatalogConnectorFactory;
import com.tytuspawlak.cinema.core.service.movie.MovieDetailsService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class MovieDetailsServiceImpl implements MovieDetailsService {
    private final MovieExternalCatalogConnectorFactory connectorFactory;

    @Override
    public Optional<MovieDetailsDTO> findMovieDetails(String id, MovieExternalCatalogType catalogType) {
        MovieExternalCatalogConnector connector = connectorFactory.get(catalogType);

        return connector.findMovieDetails(id);
    }
}
