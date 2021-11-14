package com.tytuspawlak.cinema.core.persistence.repository.movie;

import com.tytuspawlak.cinema.core.persistence.model.DBMovie;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface MovieRepository extends MongoRepository<DBMovie, String> {
    Optional<DBMovie> findByImdbId(String imdbId);
}
