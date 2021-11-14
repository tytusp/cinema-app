package com.tytuspawlak.cinema.api.controller.movie;

import com.tytuspawlak.cinema.core.dto.movie.MovieDTO;
import com.tytuspawlak.cinema.core.dto.movie.MovieDetailsDTO;
import com.tytuspawlak.cinema.core.dto.movie.MovieIdType;
import com.tytuspawlak.cinema.core.logic.movie.MovieDetailsProvider;
import com.tytuspawlak.cinema.core.service.movie.MovieService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@RestController
@RequestMapping("/api/movie")
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class MovieController {
    private final MovieService service;
    private final MovieSearchByIdStrategyProvider searchByIdStrategyProvider;
    private final MovieDetailsProvider detailsProvider;

    @GetMapping("/")
    public List<MovieDTO> findAllMovies() {
        return service.listAllMovies();
    }

    @GetMapping("/{id}")
    public MovieDTO findMovie(@PathVariable String id, @RequestParam(required = false) MovieIdType idType) {
        MovieIdType idTypeWithFallback = getIdTypeWithFallback(idType);
        Function<String, Optional<MovieDTO>> searchStrategy = searchByIdStrategyProvider.getMovieSearchByIdStrategy(idTypeWithFallback);

        return searchStrategy.apply(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found for ID=" + id + ", idType=" + idType));
    }

    @GetMapping("/{id}/details")
    public MovieDetailsDTO findMovieDetails(@PathVariable String id, @RequestParam(required = false) MovieIdType idType) {
        MovieIdType idTypeWithFallback = getIdTypeWithFallback(idType);

        return detailsProvider.findMovieDetails(id, idTypeWithFallback)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie details not found for ID=" + id + ", idType=" + idType));
    }

    private MovieIdType getIdTypeWithFallback(MovieIdType idType) {
        return Optional.ofNullable(idType)
                .orElse(MovieIdType.LOCAL_DB);
    }
}
