package com.tytuspawlak.cinema.core.logic.movie;

import com.tytuspawlak.cinema.core.dto.movie.MovieDTO;
import com.tytuspawlak.cinema.core.dto.movie.MovieIdType;
import com.tytuspawlak.cinema.core.dto.movie.MovieRatingDTO;
import com.tytuspawlak.cinema.core.logic.movie.exception.IncorrectMovieIdException;
import com.tytuspawlak.cinema.core.service.movie.MovieRatingService;
import com.tytuspawlak.cinema.core.service.movie.MovieService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class MovieRatingSaver {
    private final MovieService service;
    private final MovieRatingService ratingService;

    public MovieRatingDTO addRating(String id, MovieIdType idType, MovieRatingDTO rating) throws IncorrectMovieIdException {
        String movieId = getMovieId(id, idType)
                .orElseThrow(() -> new IncorrectMovieIdException("Movie not found for ID=" + id + ", idType=" + idType));

        MovieRatingDTO ratingToSave = rating.toBuilder()
                .movieId(movieId)
                .build();

        List<MovieRatingDTO> saveResult = ratingService.addMovieRatings(List.of(ratingToSave));

        return saveResult.stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Could not save move rating: " + ratingToSave));
    }

    private Optional<String> getMovieId(String id, MovieIdType idType) {
        if (idType == MovieIdType.LOCAL_DB) {
            return service.findMovieById(id)
                    .map(MovieDTO::getId);
        } else if (idType == MovieIdType.IMDB) {
            return service.findMovieByImdbId(id)
                    .map(MovieDTO::getId);
        } else {
            throw new UnsupportedOperationException("Not implemented MovieIdType=" + idType);
        }
    }
}
