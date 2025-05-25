package com.scheduler.scheduler.LV3.entity;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class User {

    private Long Id;

    private String name;

    private String email;

    private LocalDate createdAt;

    private LocalDate updatedAt;
}
