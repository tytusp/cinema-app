package com.tytuspawlak.cinema.core.externalcatalog.connector.omdb;

import com.tytuspawlak.cinema.core.dto.movie.MovieDetailsDTO;
import com.tytuspawlak.cinema.core.dto.movie.MovieDetailsRatingDTO;
import com.tytuspawlak.cinema.core.externalcatalog.connector.omdb.dto.OMDbApiResponseTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
class OMDbApiResponseConverter {
    MovieDetailsDTO convert(OMDbApiResponseTO response) {
        List<MovieDetailsRatingDTO> ratings = Optional.ofNullable(response.getRatings())
                .map(apiRatings -> apiRatings.stream()
                        .map(rating -> new MovieDetailsRatingDTO(rating.getSource(), rating.getValue()))
                        .collect(Collectors.toList()))
                .orElse(null);

        return MovieDetailsDTO.builder()
                .title(response.getTitle())
                .year(response.getYear())
                .rated(response.getRated())
                .released(response.getReleased())
                .runtime(response.getRuntime())
                .genre(response.getGenre())
                .director(response.getDirector())
                .writer(response.getWriter())
                .actors(response.getActors())
                .plot(response.getPlot())
                .language(response.getLanguage())
                .country(response.getCountry())
                .awards(response.getAwards())
                .poster(response.getPoster())
                .metascore(response.getMetascore())
                .imdbRating(response.getImdbRating())
                .imdbVotes(response.getImdbVotes())
                .imdbID(response.getImdbID())
                .type(response.getType())
                .dvd(response.getDvd())
                .boxOffice(response.getBoxOffice())
                .production(response.getProduction())
                .website(response.getWebsite())
                .ratings(ratings)
                .build();
    }
}
