package com.tytuspawlak.cinema.api.controller.movie;

import com.tytuspawlak.cinema.core.dto.movie.MovieDTO;
import com.tytuspawlak.cinema.core.service.movie.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/internal-api/movie")
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@Tag(name = "Movie internal", description = "Movies internal operations available for cinema administrators")
public class MovieInternalController {
    private final MovieService service;

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Adds new movies",
            description = "Adds new movies to the cinema catalog"
    )
    public Collection<MovieDTO> addMovies(Collection<MovieDTO> movies) {
        return service.addMovies(movies);
    }

    @PostMapping("/default")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Adds default movies",
            description = "Adds movies from the 'Fast & Furious' franchise that are supposed to be available in the example cinema"
    )
    public Collection<MovieDTO> addDefaultMovies() {
        List<MovieDTO> movies = List.of(
                newMovie("The Fast and the Furious",	"tt0232500"),
                newMovie("2 Fast 2 Furious", "tt0322259"),
                newMovie("The Fast and the Furious: Tokyo Drift", "tt0463985"),
                newMovie("Fast & Furious",	"tt1013752"),
                newMovie("Fast Five", "tt1596343"),
                newMovie("Fast & Furious 6", "tt1905041"),
                newMovie("Furious 7", "tt2820852"),
                newMovie("The Fate of the Furious", "tt4630562")
        );

        return service.addMovies(movies);
    }

    private MovieDTO newMovie(String title, String imdbId) {
        return MovieDTO.builder()
                .imdbId(imdbId)
                .title(title)
                .build();
    }
}
