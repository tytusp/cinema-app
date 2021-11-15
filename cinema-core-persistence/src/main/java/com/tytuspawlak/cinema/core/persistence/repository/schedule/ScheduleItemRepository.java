package com.tytuspawlak.cinema.core.persistence.repository.schedule;

import com.tytuspawlak.cinema.core.persistence.model.DBScheduleItem;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ScheduleItemRepository extends MongoRepository<DBScheduleItem, String> {
    List<DBScheduleItem> findAllByMovieIdOrderByShowTimeAsc(String movieId);
}
