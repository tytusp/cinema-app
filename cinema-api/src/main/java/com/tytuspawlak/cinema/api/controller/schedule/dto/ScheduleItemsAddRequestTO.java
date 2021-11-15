package com.tytuspawlak.cinema.api.controller.schedule.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Builder
@Jacksonized
@Value
public class ScheduleItemsAddRequestTO {
    List<ScheduleItemToSaveTO> items;
}
