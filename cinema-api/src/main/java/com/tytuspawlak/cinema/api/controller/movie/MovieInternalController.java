package com.tytuspawlak.cinema.api.controller.movie;

import com.tytuspawlak.cinema.core.dto.movie.MovieDTO;
import com.tytuspawlak.cinema.core.service.MovieService;
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
public class MovieInternalController {
    private final MovieService service;

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public Collection<MovieDTO> addMovies(Collection<MovieDTO> movies) {
        return service.addMovies(movies);
    }

    @PostMapping("/default")
    @ResponseStatus(HttpStatus.CREATED)
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
