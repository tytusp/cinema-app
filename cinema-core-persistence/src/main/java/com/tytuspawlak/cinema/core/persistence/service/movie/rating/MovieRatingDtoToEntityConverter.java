package com.tytuspawlak.cinema.core.persistence.service.movie.rating;

import com.tytuspawlak.cinema.core.dto.movie.MovieRatingDTO;
import com.tytuspawlak.cinema.core.persistence.model.DBMovieRating;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
class MovieRatingDtoToEntityConverter {
    DBMovieRating create(MovieRatingDTO rating) {
        DBMovieRating dbMovieRating = new DBMovieRating();
        dbMovieRating.setMovieId(rating.getMovieId());
        dbMovieRating.setStars(rating.getStars());
        dbMovieRating.setUser(rating.getUser());
        dbMovieRating.setReview(rating.getReview());
        dbMovieRating.setCreateDate(LocalDateTime.now());
        return dbMovieRating;
    }
}
