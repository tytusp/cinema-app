package com.tytuspawlak.cinema.api.controller.movie;

import com.tytuspawlak.cinema.core.dto.movie.MovieIdType;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
class MovieIdTypeProvider {
    MovieIdType getIdTypeWithFallback(MovieIdType idType) {
        return Optional.ofNullable(idType)
                .orElse(MovieIdType.LOCAL_DB);
    }
}
