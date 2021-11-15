package com.tytuspawlak.cinema.core.service.schedule;

import com.tytuspawlak.cinema.core.dto.schedule.ScheduleItemDTO;

import java.util.Collection;
import java.util.List;

public interface ScheduleItemService {
    List<ScheduleItemDTO> findScheduleItemsByMovieId(String movieId);

    List<ScheduleItemDTO> addScheduleItems(Collection<ScheduleItemDTO> scheduleItems);

    List<ScheduleItemDTO> updateScheduleItems(Collection<ScheduleItemDTO> scheduleItems);

    void deleteScheduleItemsByIds(Collection<String> scheduleItemIds);
}
