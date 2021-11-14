package com.tytuspawlak.cinema.core.persistence.repository.movie;

import com.tytuspawlak.cinema.core.persistence.model.DBMovieRating;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MovieRatingRepository extends MongoRepository<DBMovieRating, String> {
}
