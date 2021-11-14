package com.tytuspawlak.cinema.api.controller.movie;

import com.tytuspawlak.cinema.core.dto.movie.MovieDTO;
import com.tytuspawlak.cinema.core.dto.movie.MovieIdType;
import com.tytuspawlak.cinema.core.service.MovieService;
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

    @GetMapping("/")
    public List<MovieDTO> findAllMovies() {
        return service.listAllMovies();
    }

    @GetMapping("/{id}")
    public MovieDTO findMovie(@PathVariable String id, @RequestParam(required = false) MovieIdType idType) {
        Function<String, Optional<MovieDTO>> searchStrategy = searchByIdStrategyProvider.getMovieSearchByIdStrategy(idType);

        return searchStrategy.apply(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found for ID=" + id + ", idType=" + idType));
    }
}
