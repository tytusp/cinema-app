package com.tytuspawlak.cinema.core.externalcatalog.connector;

import com.tytuspawlak.cinema.core.dto.movie.MovieExternalCatalogType;
import com.tytuspawlak.cinema.core.externalcatalog.connector.omdb.OMDbApiConnector;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class MovieExternalCatalogConnectorFactory {
    private final OMDbApiConnector omdbApiConnector;

    public MovieExternalCatalogConnector get(MovieExternalCatalogType type) {
        if (type == MovieExternalCatalogType.OMDB) {
            return omdbApiConnector;
        }

        throw new IllegalArgumentException("Not implemented MovieExternalCatalogType=" + type);
    }
}
