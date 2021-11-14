package com.tytuspawlak.cinema.api.controller.movie;

import com.tytuspawlak.cinema.core.dto.movie.MovieDTO;
import com.tytuspawlak.cinema.core.dto.movie.MovieIdType;
import com.tytuspawlak.cinema.core.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
class MovieSearchByIdStrategyProvider {
    private final MovieService service;

    private final Map<MovieIdType, Function<String, Optional<MovieDTO>>> strategiesByIdType = Map.of(
            MovieIdType.IMDB, service::findMovieByImdbId,
            MovieIdType.LOCAL_DB, service::findMovieById
    );

    Function<String, Optional<MovieDTO>> getMovieSearchByIdStrategy(MovieIdType idType) {
        return strategiesByIdType.computeIfAbsent(idType, this::notImplemented);
    }

    private Function<String, Optional<MovieDTO>> notImplemented(MovieIdType idType) {
        throw new UnsupportedOperationException("Not implemented for idType=" + idType);
    }
}
