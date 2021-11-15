package com.tytuspawlak.cinema.core.dto.schedule;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Jacksonized
@Value
public class ScheduleItemDTO {
    String id;
    String movieId;
    LocalDateTime showTime;
    BigDecimal price;
}
