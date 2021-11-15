package com.tytuspawlak.cinema.core.persistence.service.schedule;

import com.tytuspawlak.cinema.core.dto.schedule.ScheduleItemDTO;
import com.tytuspawlak.cinema.core.persistence.model.DBScheduleItem;
import com.tytuspawlak.cinema.core.persistence.repository.schedule.ScheduleItemRepository;
import com.tytuspawlak.cinema.core.service.schedule.ScheduleItemService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class ScheduleItemServiceImpl implements ScheduleItemService {
    private final ScheduleItemRepository repository;
    private final ScheduleItemDtoToEntityConverter dtoToEntityConverter;
    private final ScheduleItemEntityToDtoConverter entityToDtoConverter;

    @Override
    public List<ScheduleItemDTO> findScheduleItemsByMovieId(String movieId) {
        List<DBScheduleItem> scheduleItems = repository.findAllByMovieIdOrderByShowTimeAsc(movieId);
        return scheduleItems.stream()
                .map(entityToDtoConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public List<ScheduleItemDTO> addScheduleItems(Collection<ScheduleItemDTO> scheduleItems) {
        List<DBScheduleItem> dbScheduleItems = scheduleItems.stream()
                .map(dtoToEntityConverter::create)
                .collect(Collectors.toList());

        List<DBScheduleItem> saveResult = repository.insert(dbScheduleItems);

        return saveResult.stream()
                .map(entityToDtoConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public List<ScheduleItemDTO> updateScheduleItems(Collection<ScheduleItemDTO> scheduleItems) {
        Map<String, ScheduleItemDTO> idsToDto = scheduleItems.stream()
                .collect(Collectors.toMap(ScheduleItemDTO::getId, Function.identity()));

        Iterable<DBScheduleItem> dbScheduleItems = repository.findAllById(idsToDto.keySet());

        dbScheduleItems.forEach(dbScheduleItem -> dtoToEntityConverter.update(dbScheduleItem, idsToDto.get(dbScheduleItem.getId())));

        List<DBScheduleItem> saveResult = repository.saveAll(dbScheduleItems);

        return saveResult.stream()
                .map(entityToDtoConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteScheduleItemsByIds(Collection<String> scheduleItemIds) {
        repository.deleteAllById(scheduleItemIds);
    }
}
