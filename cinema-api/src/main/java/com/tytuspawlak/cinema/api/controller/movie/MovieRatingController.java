package com.tytuspawlak.cinema.api.controller.movie;

import com.tytuspawlak.cinema.api.controller.movie.dto.MovieAddRatingRequestTO;
import com.tytuspawlak.cinema.core.dto.movie.MovieIdType;
import com.tytuspawlak.cinema.core.dto.movie.MovieRatingDTO;
import com.tytuspawlak.cinema.core.logic.movie.MovieRatingSaver;
import com.tytuspawlak.cinema.core.logic.movie.exception.IncorrectMovieIdException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/movie")
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class MovieRatingController {
    private final MovieIdTypeProvider idTypeProvider;
    private final MovieRatingSaver ratingSaver;

    @PostMapping("/{id}/rating")
    @ResponseStatus(HttpStatus.CREATED)
    public MovieRatingDTO addMovieRating(@PathVariable String id, @RequestBody MovieAddRatingRequestTO requestTO) {
        validateRequest(requestTO);

        MovieIdType idTypeWithFallback = idTypeProvider.getIdTypeWithFallback(requestTO.getIdType());
        MovieRatingDTO rating = convertRating(requestTO);

        try {
            return ratingSaver.addRating(id, idTypeWithFallback, rating);
        } catch (IncorrectMovieIdException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect movie ID=" + id + ", idType=" + idTypeWithFallback);
        }
    }

    private void validateRequest(MovieAddRatingRequestTO requestTO) {
        if (requestTO == null
                || requestTO.getRating() == null
                || requestTO.getRating().getStars() == null
                || requestTO.getRating().getStars() < 1
                || requestTO.getRating().getStars() > 5) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect add movie rating request: " + requestTO);
        }
    }

    private MovieRatingDTO convertRating(MovieAddRatingRequestTO requestTO) {
        return MovieRatingDTO.builder()
                .stars(requestTO.getRating().getStars())
                .user(requestTO.getRating().getUser())
                .review(requestTO.getRating().getReview())
                .build();
    }
}
