package com.tytuspawlak.cinema.core.persistence.service.schedule;

import com.tytuspawlak.cinema.core.dto.schedule.ScheduleItemDTO;
import com.tytuspawlak.cinema.core.persistence.model.DBScheduleItem;
import org.springframework.stereotype.Component;

@Component
class ScheduleItemEntityToDtoConverter {
    ScheduleItemDTO convert(DBScheduleItem dbScheduleItem) {
        return ScheduleItemDTO.builder()
                .id(dbScheduleItem.getId())
                .movieId(dbScheduleItem.getMovieId())
                .price(dbScheduleItem.getPrice())
                .showTime(dbScheduleItem.getShowTime())
                .build();
    }
}
