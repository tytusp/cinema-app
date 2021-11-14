package com.tytuspawlak.cinema.core.persistence.service.movie.rating;

import com.tytuspawlak.cinema.core.dto.movie.MovieRatingDTO;
import com.tytuspawlak.cinema.core.persistence.model.DBMovieRating;
import org.springframework.stereotype.Component;

@Component
class MovieRatingEntityToDtoConverter {
    MovieRatingDTO convert(DBMovieRating dbMovieRating) {
        return MovieRatingDTO.builder()
                .id(dbMovieRating.getId())
                .movieId(dbMovieRating.getMovieId())
                .stars(dbMovieRating.getStars())
                .user(dbMovieRating.getUser())
                .review(dbMovieRating.getReview())
                .createDate(dbMovieRating.getCreateDate())
                .build();
    }
}
