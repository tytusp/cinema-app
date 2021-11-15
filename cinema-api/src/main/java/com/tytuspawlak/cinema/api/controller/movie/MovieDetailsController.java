package com.tytuspawlak.cinema.api.controller.movie;

import com.tytuspawlak.cinema.core.dto.movie.MovieDetailsDTO;
import com.tytuspawlak.cinema.core.dto.movie.MovieIdType;
import com.tytuspawlak.cinema.core.logic.movie.MovieDetailsProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/movie")
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@Tag(name = "Movie details", description = "Movie details lookup")
public class MovieDetailsController {
    private final MovieIdTypeProvider idTypeProvider;
    private final MovieDetailsProvider detailsProvider;

    @GetMapping("/{id}/details")
    @Operation(
            summary = "Finds movie's detailed information",
            description = "Finds movie's detailed information using OMDb API by its ID or IMDb ID"
    )
    public MovieDetailsDTO findMovieDetails(@PathVariable String id, @RequestParam(required = false) MovieIdType idType) {
        MovieIdType idTypeWithFallback = idTypeProvider.getIdTypeWithFallback(idType);

        return detailsProvider.findMovieDetails(id, idTypeWithFallback)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie details not found for ID=" + id + ", idType=" + idType));
    }
}
