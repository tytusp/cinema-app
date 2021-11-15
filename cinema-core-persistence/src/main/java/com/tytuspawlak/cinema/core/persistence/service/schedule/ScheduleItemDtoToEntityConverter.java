package com.tytuspawlak.cinema.core.persistence.service.schedule;

import com.tytuspawlak.cinema.core.dto.schedule.ScheduleItemDTO;
import com.tytuspawlak.cinema.core.persistence.model.DBScheduleItem;
import org.springframework.stereotype.Component;

@Component
class ScheduleItemDtoToEntityConverter {
    DBScheduleItem create(ScheduleItemDTO scheduleItem) {
        DBScheduleItem dbScheduleItem = new DBScheduleItem();
        dbScheduleItem.setMovieId(scheduleItem.getMovieId());

        update(dbScheduleItem, scheduleItem);

        return dbScheduleItem;
    }

    void update(DBScheduleItem dbScheduleItem, ScheduleItemDTO scheduleItem) {
        dbScheduleItem.setPrice(scheduleItem.getPrice());
        dbScheduleItem.setShowTime(scheduleItem.getShowTime());
    }
}
