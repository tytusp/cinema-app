package com.tytuspawlak.cinema.core.persistence.service.movie.rating;

import com.tytuspawlak.cinema.core.dto.movie.MovieRatingDTO;
import com.tytuspawlak.cinema.core.persistence.model.DBMovieRating;
import com.tytuspawlak.cinema.core.persistence.repository.movie.MovieRatingRepository;
import com.tytuspawlak.cinema.core.service.movie.MovieRatingService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class MovieRatingServiceImpl implements MovieRatingService {
    private final MovieRatingRepository repository;
    private final MovieRatingDtoToEntityConverter dtoToEntityConverter;
    private final MovieRatingEntityToDtoConverter entityToDtoConverter;

    @Override
    public List<MovieRatingDTO> addMovieRatings(Collection<MovieRatingDTO> ratings) {
        List<DBMovieRating> ratingsToSave = ratings.stream()
                .map(dtoToEntityConverter::create)
                .collect(Collectors.toList());

        List<DBMovieRating> saveResult = repository.insert(ratingsToSave);

        return saveResult.stream()
                .map(entityToDtoConverter::convert)
                .collect(Collectors.toList());
    }
}
