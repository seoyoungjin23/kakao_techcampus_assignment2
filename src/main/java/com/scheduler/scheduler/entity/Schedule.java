package com.scheduler.scheduler.entity;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Schedule {

    private Long scheduleId;

    private String author;

    private String password;

    private String todo;

    private LocalDate createdAt;

    private LocalDate updatedAt;

}
