package com.scheduler.scheduler.LV3.service;

import com.scheduler.scheduler.LV3.dto.ScheduleRequestDto;
import com.scheduler.scheduler.LV3.dto.ScheduleResponseDto;
import java.time.LocalDate;
import java.util.List;

public interface ScheduleService {

    ScheduleResponseDto createSchedule(ScheduleRequestDto scheduleRequestDto);

    ScheduleResponseDto getScheduleById(Long scheduleId);

    List<ScheduleResponseDto> getSchedulesByAuthorAndUpdatedAt(String author, LocalDate updatedAt);

    ScheduleResponseDto updateSchedule(Long scheduleId, ScheduleRequestDto dto);

    void deleteSchedule(Long scheduleId, String password);
}
