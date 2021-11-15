package com.tytuspawlak.cinema.api.controller.schedule.dto;

import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Jacksonized
@Value
public class ScheduleItemToSaveTO {
    BigDecimal price;
    LocalDateTime showTime;
}
