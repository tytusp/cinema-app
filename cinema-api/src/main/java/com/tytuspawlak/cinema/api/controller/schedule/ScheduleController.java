package com.tytuspawlak.cinema.api.controller.schedule;

import com.tytuspawlak.cinema.core.dto.schedule.ScheduleItemDTO;
import com.tytuspawlak.cinema.core.service.schedule.ScheduleItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Schedule", description = "Cinema schedule operations available for regular users")
public class ScheduleController {
    private final ScheduleItemService service;

    @GetMapping("/{movieId}/item")
    @Operation(
            summary = "Finds schedule items for a movie",
            description = "Finds schedule items (single show descriptors containing show time and price) for a movie specified by its ID"
    )
    public List<ScheduleItemDTO> findMovieScheduleItems(@PathVariable String movieId) {
        return service.findScheduleItemsByMovieId(movieId);
    }
}
