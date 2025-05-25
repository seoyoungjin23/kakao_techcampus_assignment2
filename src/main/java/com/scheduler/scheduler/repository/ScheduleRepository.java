package com.scheduler.scheduler.repository;

import com.scheduler.scheduler.entity.Schedule;
import java.time.LocalDate;
import java.util.List;

public interface ScheduleRepository {

    Schedule save(Schedule schedule);

    Schedule findById(Long id);

    List<Schedule> findSchedulesByAuthorAndUpdatedAt(String author, LocalDate updatedAt);

    Schedule update(Long id, Schedule schedule);

    void delete(Long scheduleId);
}
