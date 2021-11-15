package com.tytuspawlak.cinema.api.controller.movie;

import com.tytuspawlak.cinema.core.dto.movie.MovieDTO;
import com.tytuspawlak.cinema.core.dto.movie.MovieIdType;
import com.tytuspawlak.cinema.core.service.movie.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Movie", description = "Movie basic operations available for regular users")
public class MovieController {
    private final MovieService service;
    private final MovieSearchByIdStrategyProvider searchByIdStrategyProvider;
    private final MovieIdTypeProvider idTypeProvider;

    @GetMapping("/")
    @Operation(
            summary = "Finds all movies",
            description = "Finds all movies basic info (ID, title and IMDb ID)"
    )
    public List<MovieDTO> findAllMovies() {
        return service.listAllMovies();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Finds a movie",
            description = "Finds a movie's basic info (ID, title and IMDb ID) by ID or IMDb ID"
    )
    public MovieDTO findMovie(@PathVariable String id, @RequestParam(required = false) MovieIdType idType) {
        MovieIdType idTypeWithFallback = idTypeProvider.getIdTypeWithFallback(idType);
        Function<String, Optional<MovieDTO>> searchStrategy = searchByIdStrategyProvider.getMovieSearchByIdStrategy(idTypeWithFallback);

        return searchStrategy.apply(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found for ID=" + id + ", idType=" + idType));
    }
}
