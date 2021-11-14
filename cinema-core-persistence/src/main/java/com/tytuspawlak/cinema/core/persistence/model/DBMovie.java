package com.tytuspawlak.cinema.core.persistence.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("movies")
public class DBMovie {
    private String id;
    private String title;
    private String imdbId;
}
