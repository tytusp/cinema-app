package com.tytuspawlak.cinema.api.controller.movie;

import com.tytuspawlak.cinema.core.dto.movie.MovieDTO;
import com.tytuspawlak.cinema.core.dto.movie.MovieIdType;
import com.tytuspawlak.cinema.core.service.movie.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
class MovieSearchByIdStrategyProvider {
    private static final Map<MovieIdType, Function<MovieService, Function<String, Optional<MovieDTO>>>> STRATEGIES_BY_ID_TYPE = Map.of(
            MovieIdType.IMDB, service -> service::findMovieByImdbId,
            MovieIdType.LOCAL_DB, service -> service::findMovieById
    );
    private final MovieService service;


    Function<String, Optional<MovieDTO>> getMovieSearchByIdStrategy(MovieIdType idType) {
        return STRATEGIES_BY_ID_TYPE.computeIfAbsent(idType, this::notImplemented)
                .apply(service);
    }

    private Function<MovieService, Function<String, Optional<MovieDTO>>> notImplemented(MovieIdType idType) {
        throw new UnsupportedOperationException("Not implemented for idType=" + idType);
    }
}
