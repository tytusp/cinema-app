package com.tytuspawlak.cinema.core.persistence.service.movie;

import com.tytuspawlak.cinema.core.dto.movie.MovieDTO;
import com.tytuspawlak.cinema.core.dto.movie.MovieSearchFilter;
import com.tytuspawlak.cinema.core.persistence.model.DBMovie;
import com.tytuspawlak.cinema.core.persistence.repository.movie.MovieRepository;
import com.tytuspawlak.cinema.core.service.MovieService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class MovieServiceImpl implements MovieService {
    private final MovieRepository repository;
    private final MovieEntityToDtoConverter entityToDtoConverter;
    private final MovieDtoToEntityConverter dtoToEntityConverter;

    @Override
    public List<MovieDTO> listAllMovies() {
        return repository.findAll().stream()
                .map(entityToDtoConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public List<MovieDTO> addMovies(Collection<MovieDTO> movies) {
        List<DBMovie> dbMovies = movies.stream()
                .map(dtoToEntityConverter::create)
                .collect(Collectors.toList());

        List<DBMovie> saveResult = repository.insert(dbMovies);

        return saveResult.stream()
                .map(entityToDtoConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public List<MovieDTO> searchMovies(MovieSearchFilter searchFilter) {
        return null;
    }

    @Override
    public Optional<MovieDTO> findMovieById(String id) {
        return repository.findById(id)
                .map(entityToDtoConverter::convert);
    }

    @Override
    public Optional<MovieDTO> findMovieByImdbId(String imdbId) {
        return repository.findByImdbId(imdbId)
                .map(entityToDtoConverter::convert);
    }
}
