package com.tytuspawlak.cinema.core.logic.movie;

import com.tytuspawlak.cinema.core.dto.movie.MovieDTO;
import com.tytuspawlak.cinema.core.dto.movie.MovieDetailsDTO;
import com.tytuspawlak.cinema.core.dto.movie.MovieExternalCatalogType;
import com.tytuspawlak.cinema.core.dto.movie.MovieIdType;
import com.tytuspawlak.cinema.core.service.movie.MovieDetailsService;
import com.tytuspawlak.cinema.core.service.movie.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieDetailsProviderTest {
    private static final String MOVIE_IMDB_DB_ID = "tt0322259";
    private static final String MOVIE_LOCAL_DB_ID = "6190540270abdf44d8b14dcd";

    @Mock
    private MovieService service;
    @Mock
    private MovieDetailsService detailsService;

    private MovieDetailsProvider detailsProvider;

    @BeforeEach
    void init() {
        detailsProvider = new MovieDetailsProvider(service, detailsService);
    }

    @Test
    void testGetDetailsByImdbId_Success() {
        MovieDetailsDTO expectedMovieDetails = MovieDetailsDTO.builder()
                .imdbID(MOVIE_IMDB_DB_ID)
                .imdbRating("5.7")
                .build();
        when(detailsService.findMovieDetails(MOVIE_IMDB_DB_ID, MovieExternalCatalogType.OMDB)).thenReturn(Optional.of(expectedMovieDetails));

        Optional<MovieDetailsDTO> actual = detailsProvider.findMovieDetails(MOVIE_IMDB_DB_ID, MovieIdType.IMDB);

        assertTrue(actual.isPresent());
        assertSame(expectedMovieDetails, actual.get());

        verifyNoMoreInteractions(service, detailsService);
    }

    @Test
    void testGetDetailsByImdbId_NotFound() {
        when(detailsService.findMovieDetails(MOVIE_IMDB_DB_ID, MovieExternalCatalogType.OMDB)).thenReturn(Optional.empty());

        Optional<MovieDetailsDTO> actual = detailsProvider.findMovieDetails(MOVIE_IMDB_DB_ID, MovieIdType.IMDB);

        assertTrue(actual.isEmpty());

        verifyNoMoreInteractions(service, detailsService);
    }

    @Test
    void testGetDetailsByLocalDbId_Success() {
        MovieDetailsDTO expectedMovieDetails = MovieDetailsDTO.builder()
                .imdbID(MOVIE_IMDB_DB_ID)
                .imdbRating("5.7")
                .build();
        when(detailsService.findMovieDetails(MOVIE_IMDB_DB_ID, MovieExternalCatalogType.OMDB)).thenReturn(Optional.of(expectedMovieDetails));
        when(service.findMovieById(MOVIE_LOCAL_DB_ID)).thenReturn(Optional.of(
                MovieDTO.builder()
                        .id(MOVIE_LOCAL_DB_ID)
                        .imdbId(MOVIE_IMDB_DB_ID)
                        .build())
        );

        Optional<MovieDetailsDTO> actual = detailsProvider.findMovieDetails(MOVIE_LOCAL_DB_ID, MovieIdType.LOCAL_DB);

        assertTrue(actual.isPresent());
        assertSame(expectedMovieDetails, actual.get());

        verifyNoMoreInteractions(service, detailsService);
    }

    @Test
    void testGetDetailsByLocalDbId_NotFoundInLocalDb() {
        when(service.findMovieById(MOVIE_LOCAL_DB_ID)).thenReturn(Optional.empty());

        Optional<MovieDetailsDTO> actual = detailsProvider.findMovieDetails(MOVIE_LOCAL_DB_ID, MovieIdType.LOCAL_DB);

        assertTrue(actual.isEmpty());

        verifyNoMoreInteractions(service, detailsService);
    }

    @Test
    void testGetDetailsByLocalDbId_NotFoundInDetailsLookup() {
        when(detailsService.findMovieDetails(MOVIE_IMDB_DB_ID, MovieExternalCatalogType.OMDB)).thenReturn(Optional.empty());
        when(service.findMovieById(MOVIE_LOCAL_DB_ID)).thenReturn(Optional.of(
                MovieDTO.builder()
                        .id(MOVIE_LOCAL_DB_ID)
                        .imdbId(MOVIE_IMDB_DB_ID)
                        .build())
        );

        Optional<MovieDetailsDTO> actual = detailsProvider.findMovieDetails(MOVIE_LOCAL_DB_ID, MovieIdType.LOCAL_DB);

        assertTrue(actual.isEmpty());

        verifyNoMoreInteractions(service, detailsService);
    }

    @Test
    void testNotImplementedIdType() {

        UnsupportedOperationException thrown = assertThrows(
                UnsupportedOperationException.class,
                () -> detailsProvider.findMovieDetails(MOVIE_LOCAL_DB_ID, null)
        );

        assertEquals("Not implemented idType=null", thrown.getMessage());

        verifyNoInteractions(service, detailsService);
    }
}