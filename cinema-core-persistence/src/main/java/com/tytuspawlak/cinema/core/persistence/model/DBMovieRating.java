package com.tytuspawlak.cinema.core.persistence.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document("movies.ratings")
public class DBMovieRating {
    private String id;
    private String movieId;
    private Integer stars;
    private String user;
    private String review;
    private LocalDateTime createDate;
}
