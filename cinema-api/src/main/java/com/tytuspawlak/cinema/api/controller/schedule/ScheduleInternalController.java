package com.tytuspawlak.cinema.api.controller.schedule;

import com.tytuspawlak.cinema.api.controller.schedule.dto.ScheduleItemToSaveTO;
import com.tytuspawlak.cinema.api.controller.schedule.dto.ScheduleItemsAddRequestTO;
import com.tytuspawlak.cinema.api.controller.schedule.dto.ScheduleItemsUpdateRequestTO;
import com.tytuspawlak.cinema.core.dto.schedule.ScheduleItemDTO;
import com.tytuspawlak.cinema.core.service.movie.MovieService;
import com.tytuspawlak.cinema.core.service.schedule.ScheduleItemService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/internal-api/schedule")
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class ScheduleInternalController {
    private final MovieService movieService;
    private final ScheduleItemService service;

    @PostMapping("/{movieId}/item")
    @ResponseStatus(HttpStatus.CREATED)
    public List<ScheduleItemDTO> addMovieScheduleItems(@PathVariable String movieId, @RequestBody ScheduleItemsAddRequestTO requestTO) {
        validateAddRequest(requestTO, movieId);

        List<ScheduleItemDTO> scheduleItems = requestTO.getItems().stream()
                .map(item -> convertScheduleItem(item, movieId, null))
                .collect(Collectors.toList());

        return service.addScheduleItems(scheduleItems);
    }

    @PutMapping("/{movieId}/item/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ScheduleItemDTO updateMovieScheduleItem(@PathVariable String movieId, @PathVariable String itemId, @RequestBody ScheduleItemsUpdateRequestTO requestTO) {
        validateUpdateRequest(requestTO, movieId);

        ScheduleItemDTO scheduleItem = convertScheduleItem(requestTO.getItem(), movieId, itemId);

        List<ScheduleItemDTO> saveResult = service.updateScheduleItems(List.of(scheduleItem));

        return saveResult.stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Could not update schedule item: " + requestTO));
    }

    @DeleteMapping("/{movieId}/item/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMovieScheduleItem(@PathVariable String movieId, @PathVariable String itemId) {
        validateMovieId(movieId);

        service.deleteScheduleItemsByIds(List.of(itemId));
    }

    private void validateAddRequest(ScheduleItemsAddRequestTO requestTO, String movieId) {
        if (requestTO == null
                || requestTO.getItems() == null
                || requestTO.getItems().isEmpty()
                || requestTO.getItems().stream()
                        .anyMatch(this::isScheduleItemInvalid)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect schedule item save request: " + requestTO);
        }

        validateMovieId(movieId);
    }

    private void validateUpdateRequest(ScheduleItemsUpdateRequestTO requestTO, String movieId) {
        if (requestTO == null
                || isScheduleItemInvalid(requestTO.getItem())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect schedule item save request: " + requestTO);
        }

        validateMovieId(movieId);
    }

    private boolean isScheduleItemInvalid(ScheduleItemToSaveTO item) {
        return item == null
                || item.getShowTime() == null
                || item.getPrice() == null
                || item.getPrice().compareTo(BigDecimal.ZERO) < 0;
    }

    private void validateMovieId(String movieId) {
        if (movieService.findMovieById(movieId).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect movie ID: " + movieId);
        }
    }

    private ScheduleItemDTO convertScheduleItem(ScheduleItemToSaveTO item, String movieId, String itemId) {
        return ScheduleItemDTO.builder()
                .id(itemId)
                .movieId(movieId)
                .price(item.getPrice())
                .showTime(item.getShowTime())
                .build();
    }
}
