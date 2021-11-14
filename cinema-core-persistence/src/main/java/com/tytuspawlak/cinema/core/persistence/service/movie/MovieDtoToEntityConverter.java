package com.tytuspawlak.cinema.core.persistence.service.movie;

import com.tytuspawlak.cinema.core.dto.movie.MovieDTO;
import com.tytuspawlak.cinema.core.persistence.model.DBMovie;
import org.springframework.stereotype.Component;

@Component
class MovieDtoToEntityConverter {
    DBMovie create(MovieDTO movie) {
        DBMovie dbMovie = new DBMovie();
        dbMovie.setImdbId(movie.getImdbId());
        dbMovie.setTitle(movie.getTitle());
        return dbMovie;
    }
}
