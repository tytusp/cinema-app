package com.tytuspawlak.cinema.core.logic.movie;

import com.tytuspawlak.cinema.core.dto.movie.MovieDTO;
import com.tytuspawlak.cinema.core.dto.movie.MovieDetailsDTO;
import com.tytuspawlak.cinema.core.dto.movie.MovieExternalCatalogType;
import com.tytuspawlak.cinema.core.dto.movie.MovieIdType;
import com.tytuspawlak.cinema.core.service.movie.MovieDetailsService;
import com.tytuspawlak.cinema.core.service.movie.MovieService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class MovieDetailsProvider {
    private final MovieService service;
    private final MovieDetailsService detailsService;

    public Optional<MovieDetailsDTO> findMovieDetails(String id, MovieIdType idType) {
        Optional<String> imdbId = getImdbId(id, idType);

        return imdbId.flatMap(this::getDetailsFromOmdb);
    }

    private Optional<String> getImdbId(String id, MovieIdType idType) {
        if (idType == MovieIdType.IMDB) {
            return Optional.of(id);
        } else if (idType == MovieIdType.LOCAL_DB) {
            return service.findMovieById(id)
                    .map(MovieDTO::getImdbId);
        } else {
            throw new UnsupportedOperationException("Not implemented idType=" + idType);
        }
    }

    private Optional<MovieDetailsDTO> getDetailsFromOmdb(String imdbId) {
        return detailsService.findMovieDetails(imdbId, MovieExternalCatalogType.OMDB);
    }
}
