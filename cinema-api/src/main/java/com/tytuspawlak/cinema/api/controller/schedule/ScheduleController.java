package com.tytuspawlak.cinema.api.controller.schedule;

import com.tytuspawlak.cinema.core.dto.schedule.ScheduleItemDTO;
import com.tytuspawlak.cinema.core.service.schedule.ScheduleItemService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/schedule")
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class ScheduleController {
    private final ScheduleItemService service;

    @GetMapping("/{movieId}/item")
    public List<ScheduleItemDTO> findMovieScheduleItems(@PathVariable String movieId) {
        return service.findScheduleItemsByMovieId(movieId);
    }
}
