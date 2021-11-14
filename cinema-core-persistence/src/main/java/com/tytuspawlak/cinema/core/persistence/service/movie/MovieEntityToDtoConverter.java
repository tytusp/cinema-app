package com.tytuspawlak.cinema.core.persistence.service.movie;

import com.tytuspawlak.cinema.core.dto.movie.MovieDTO;
import com.tytuspawlak.cinema.core.persistence.model.DBMovie;
import org.springframework.stereotype.Component;

@Component
class MovieEntityToDtoConverter {
    MovieDTO convert(DBMovie dbMovie) {
        return MovieDTO.builder()
                .id(dbMovie.getId())
                .imdbId(dbMovie.getImdbId())
                .title(dbMovie.getTitle())
                .build();
    }
}
