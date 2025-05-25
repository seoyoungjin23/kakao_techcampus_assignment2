package com.scheduler.scheduler.LV3.repository;

import com.scheduler.scheduler.LV3.entity.Schedule;
import java.time.LocalDate;
import java.util.List;

public interface ScheduleRepository {

    Schedule save(Schedule schedule);

    Schedule findById(Long id);

    List<Schedule> findSchedulesByAuthorAndUpdatedAt(String author, LocalDate updatedAt);

    Schedule update(Long id, Schedule schedule);

    void delete(Long scheduleId);
}
