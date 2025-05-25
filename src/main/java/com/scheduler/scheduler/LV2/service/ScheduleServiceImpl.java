package com.scheduler.scheduler.LV2.service;

import com.scheduler.scheduler.LV2.dto.ScheduleRequestDto;
import com.scheduler.scheduler.LV2.dto.ScheduleResponseDto;
import com.scheduler.scheduler.LV2.entity.Schedule;
import com.scheduler.scheduler.LV2.repository.ScheduleRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;

    @Override
    @Transactional
    public ScheduleResponseDto createSchedule(ScheduleRequestDto scheduleRequestDto) {

        LocalDate now = LocalDate.now();

        Schedule schedule = Schedule.builder()
                .todo(scheduleRequestDto.getTodo())
                .author(scheduleRequestDto.getAuthor())
                .password(scheduleRequestDto.getPassword())
                .createdAt(now)
                .updatedAt(now)
                .build();

        Schedule savedSchedule = scheduleRepository.save(schedule);

        return ScheduleResponseDto.from(savedSchedule);
    }

    @Override
    @Transactional(readOnly = true)
    public ScheduleResponseDto getScheduleById(Long scheduleId) {

        Schedule schedule = scheduleRepository.findById(scheduleId);

        return ScheduleResponseDto.from(schedule);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScheduleResponseDto> getSchedulesByAuthorAndUpdatedAt(String author, LocalDate updatedAt) {

        return scheduleRepository.findSchedulesByAuthorAndUpdatedAt(author, updatedAt)
                .stream()
                .map(ScheduleResponseDto::from)
                .collect(Collectors.toList());

    }

    @Override
    @Transactional
    public ScheduleResponseDto updateSchedule(Long scheduleId, ScheduleRequestDto scheduleRequestDto) {
        Schedule schedule = scheduleRepository.findById(scheduleId);

        if (!schedule.getPassword().equals(scheduleRequestDto.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        Schedule updatedSchedule = Schedule.builder()
                .scheduleId(scheduleId)
                .todo(scheduleRequestDto.getTodo())
                .author(scheduleRequestDto.getAuthor())
                .password(scheduleRequestDto.getPassword())
                .createdAt(schedule.getCreatedAt())
                .updatedAt(LocalDate.now())
                .build();

        Schedule savedSchedule = scheduleRepository.update(scheduleId, updatedSchedule);
        return ScheduleResponseDto.from(savedSchedule);
    }

    @Override
    @Transactional
    public void deleteSchedule(Long scheduleId, String password) {
        Schedule schedule = scheduleRepository.findById(scheduleId);

        if (!schedule.getPassword().equals(password)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        scheduleRepository.delete(scheduleId);
    }

}
