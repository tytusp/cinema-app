package com.tytuspawlak.cinema.core.persistence.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Document("schedule.items")
public class DBScheduleItem {
    private String id;
    private String movieId;
    private LocalDateTime showTime;
    private BigDecimal price;
}
