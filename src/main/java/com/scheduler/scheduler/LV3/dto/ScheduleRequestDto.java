package com.scheduler.scheduler.LV3.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ScheduleRequestDto {

    private String todo;

    private String author;

    private String email;

    private String password;

}
