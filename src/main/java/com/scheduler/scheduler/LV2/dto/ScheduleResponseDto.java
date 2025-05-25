package com.scheduler.scheduler.LV2.dto;

import com.scheduler.scheduler.LV2.entity.Schedule;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ScheduleResponseDto {

    private Long scheduleId;

    private String todo;

    private String author;

    private LocalDate createdAt;

    private LocalDate updatedAt;

    public static ScheduleResponseDto from(Schedule schedule) {

        return ScheduleResponseDto.builder()
                .scheduleId(schedule.getScheduleId())
                .todo(schedule.getTodo())
                .author(schedule.getAuthor())
                .createdAt(schedule.getCreatedAt())
                .updatedAt(schedule.getUpdatedAt())
                .build();
    }
}
