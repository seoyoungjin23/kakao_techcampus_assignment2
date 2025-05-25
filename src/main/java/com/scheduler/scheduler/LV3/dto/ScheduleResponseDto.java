package com.scheduler.scheduler.LV3.dto;

import com.scheduler.scheduler.LV3.entity.Schedule;
import com.scheduler.scheduler.LV3.entity.User;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ScheduleResponseDto {

    private Long scheduleId;

    private Long userId;

    private String todo;

    private String author;

    private String email;

    private LocalDate createdAt;

    private LocalDate updatedAt;

    public static ScheduleResponseDto from(Schedule schedule, User user) {

        return ScheduleResponseDto.builder()
                .scheduleId(schedule.getScheduleId())
                .todo(schedule.getTodo())
                .author(user.getName())
                .userId(user.getId())
                .email(user.getEmail())
                .createdAt(schedule.getCreatedAt())
                .updatedAt(schedule.getUpdatedAt())
                .build();
    }
}
