package com.tytuspawlak.cinema.api.controller.schedule.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
@Value
public class ScheduleItemsUpdateRequestTO {
    ScheduleItemToSaveTO item;
}
