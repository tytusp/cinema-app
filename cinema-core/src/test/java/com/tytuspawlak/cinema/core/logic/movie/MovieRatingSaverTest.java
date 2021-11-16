package com.tytuspawlak.cinema.core.logic.movie;

import com.tytuspawlak.cinema.core.dto.movie.MovieDTO;
import com.tytuspawlak.cinema.core.dto.movie.MovieIdType;
import com.tytuspawlak.cinema.core.dto.movie.MovieRatingDTO;
import com.tytuspawlak.cinema.core.logic.movie.exception.IncorrectMovieIdException;
import com.tytuspawlak.cinema.core.service.movie.MovieRatingService;
import com.tytuspawlak.cinema.core.service.movie.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieRatingSaverTest {
    private static final String MOVIE_IMDB_DB_ID = "tt0322259";
    private static final String MOVIE_LOCAL_DB_ID = "6190540270abdf44d8b14dcd";

    @Mock
    private MovieService service;
    @Mock
    private MovieRatingService ratingService;
    @Captor
    private ArgumentCaptor<Collection<MovieRatingDTO>> ratingToSaveCaptor;

    private MovieRatingSaver ratingSaver;

    @BeforeEach
    void init() {
        ratingSaver = new MovieRatingSaver(service, ratingService);
    }

    @Test
    void testExistingMovie_AddByImdbId() throws IncorrectMovieIdException {
        MovieRatingDTO expectedSaveResult = MovieRatingDTO.builder()
                .id("6191aa740fd5f2208d300a80")
                .movieId(MOVIE_LOCAL_DB_ID)
                .stars(5)
                .user("a user")
                .review("Fascinating!")
                .createDate(LocalDateTime.now())
                .build();
        when(ratingService.addMovieRatings(anyCollection())).thenReturn(List.of(expectedSaveResult));

        when(service.findMovieByImdbId(MOVIE_IMDB_DB_ID)).thenReturn(Optional.of(
                MovieDTO.builder()
                        .id(MOVIE_LOCAL_DB_ID)
                        .build())
        );

        MovieRatingDTO rating = MovieRatingDTO.builder()
                .stars(5)
                .user("a user")
                .review("Fascinating!")
                .build();

        MovieRatingDTO actual = ratingSaver.addRating(MOVIE_IMDB_DB_ID, MovieIdType.IMDB, rating);

        assertSame(expectedSaveResult, actual);

        verify(ratingService).addMovieRatings(ratingToSaveCaptor.capture());
        MovieRatingDTO expectedRatingToSave = MovieRatingDTO.builder()
                .movieId(MOVIE_LOCAL_DB_ID)
                .stars(5)
                .user("a user")
                .review("Fascinating!")
                .build();
        assertEquals(1, ratingToSaveCaptor.getValue().size());
        assertEquals(expectedRatingToSave, ratingToSaveCaptor.getValue().stream().findFirst().get());

        verifyNoMoreInteractions(ratingService, service);
    }

    @Test
    void testExistingMovie_AddByLocalDbId() throws IncorrectMovieIdException {
        MovieRatingDTO expectedSaveResult = MovieRatingDTO.builder()
                .id("6345bb740fd5f2208d300a80")
                .movieId(MOVIE_LOCAL_DB_ID)
                .stars(2)
                .user("other user")
                .review("Boring!")
                .createDate(LocalDateTime.now())
                .build();
        when(ratingService.addMovieRatings(anyCollection())).thenReturn(List.of(expectedSaveResult));

        when(service.findMovieById(MOVIE_LOCAL_DB_ID)).thenReturn(Optional.of(
                MovieDTO.builder()
                        .id(MOVIE_LOCAL_DB_ID)
                        .build())
        );

        MovieRatingDTO rating = MovieRatingDTO.builder()
                .stars(2)
                .user("other user")
                .review("Boring!")
                .build();

        MovieRatingDTO actual = ratingSaver.addRating(MOVIE_LOCAL_DB_ID, MovieIdType.LOCAL_DB, rating);

        assertSame(expectedSaveResult, actual);

        verify(ratingService).addMovieRatings(ratingToSaveCaptor.capture());
        MovieRatingDTO expectedRatingToSave = MovieRatingDTO.builder()
                .movieId(MOVIE_LOCAL_DB_ID)
                .stars(2)
                .user("other user")
                .review("Boring!")
                .build();
        assertEquals(1, ratingToSaveCaptor.getValue().size());
        assertEquals(expectedRatingToSave, ratingToSaveCaptor.getValue().stream().findFirst().get());

        verifyNoMoreInteractions(ratingService, service);
    }

    @Test
    void testNonExistingMovie_AddByImdbId() {
        when(service.findMovieByImdbId(MOVIE_IMDB_DB_ID)).thenReturn(Optional.empty());

        MovieRatingDTO rating = MovieRatingDTO.builder()
                .stars(3)
                .user("user name")
                .review("Average")
                .build();

        IncorrectMovieIdException thrown = assertThrows(
                IncorrectMovieIdException.class,
                () -> ratingSaver.addRating(MOVIE_IMDB_DB_ID, MovieIdType.IMDB, rating)
        );


        assertEquals("Movie not found for ID=" + MOVIE_IMDB_DB_ID +", idType=IMDB", thrown.getMessage());

        verifyNoMoreInteractions(ratingService, service);
    }

    @Test
    void testNonExistingMovie_AddByLocalDbId() {
        when(service.findMovieById(MOVIE_LOCAL_DB_ID)).thenReturn(Optional.empty());

        MovieRatingDTO rating = MovieRatingDTO.builder()
                .stars(3)
                .user("user name")
                .review("Average")
                .build();

        IncorrectMovieIdException thrown = assertThrows(
                IncorrectMovieIdException.class,
                () -> ratingSaver.addRating(MOVIE_LOCAL_DB_ID, MovieIdType.LOCAL_DB, rating)
        );


        assertEquals("Movie not found for ID=" + MOVIE_LOCAL_DB_ID +", idType=LOCAL_DB", thrown.getMessage());

        verifyNoMoreInteractions(ratingService, service);
    }

    @Test
    void testExistingMovie_AddByImdbDbId_NotSaved() {
        when(service.findMovieByImdbId(MOVIE_IMDB_DB_ID)).thenReturn(Optional.of(
                MovieDTO.builder()
                        .id(MOVIE_LOCAL_DB_ID)
                        .build())
        );

        MovieRatingDTO rating = MovieRatingDTO.builder()
                .stars(2)
                .user("other user")
                .review("Boring!")
                .build();

        IllegalStateException thrown = assertThrows(
                IllegalStateException.class,
                () -> ratingSaver.addRating(MOVIE_IMDB_DB_ID, MovieIdType.IMDB, rating)
        );

        verify(ratingService).addMovieRatings(ratingToSaveCaptor.capture());
        MovieRatingDTO expectedRatingToSave = MovieRatingDTO.builder()
                .movieId(MOVIE_LOCAL_DB_ID)
                .stars(2)
                .user("other user")
                .review("Boring!")
                .build();
        assertEquals(1, ratingToSaveCaptor.getValue().size());
        assertEquals(expectedRatingToSave, ratingToSaveCaptor.getValue().stream().findFirst().get());
        assertEquals("Could not save movie rating: " + expectedRatingToSave, thrown.getMessage());

        verifyNoMoreInteractions(ratingService, service);
    }

    @Test
    void testExistingMovie_AddByLocalDbId_NotSaved() {
        when(service.findMovieById(MOVIE_LOCAL_DB_ID)).thenReturn(Optional.of(
                MovieDTO.builder()
                        .id(MOVIE_LOCAL_DB_ID)
                        .build())
        );

        MovieRatingDTO rating = MovieRatingDTO.builder()
                .stars(2)
                .user("other user")
                .review("Boring!")
                .build();

        IllegalStateException thrown = assertThrows(
                IllegalStateException.class,
                () -> ratingSaver.addRating(MOVIE_LOCAL_DB_ID, MovieIdType.LOCAL_DB, rating)
        );

        verify(ratingService).addMovieRatings(ratingToSaveCaptor.capture());
        MovieRatingDTO expectedRatingToSave = MovieRatingDTO.builder()
                .movieId(MOVIE_LOCAL_DB_ID)
                .stars(2)
                .user("other user")
                .review("Boring!")
                .build();
        assertEquals(1, ratingToSaveCaptor.getValue().size());
        assertEquals(expectedRatingToSave, ratingToSaveCaptor.getValue().stream().findFirst().get());
        assertEquals("Could not save movie rating: " + expectedRatingToSave, thrown.getMessage());

        verifyNoMoreInteractions(ratingService, service);
    }
}