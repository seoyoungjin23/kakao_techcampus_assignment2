package com.scheduler.scheduler.LV3.service;

import com.scheduler.scheduler.LV3.dto.ScheduleRequestDto;
import com.scheduler.scheduler.LV3.dto.ScheduleResponseDto;
import com.scheduler.scheduler.LV3.entity.Schedule;
import com.scheduler.scheduler.LV3.entity.User;
import com.scheduler.scheduler.LV3.repository.ScheduleRepository;
import com.scheduler.scheduler.LV3.repository.UserRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;

    private final UserRepository userRepository;

    @Override
    @Transactional
    public ScheduleResponseDto createSchedule(ScheduleRequestDto scheduleRequestDto) {

        LocalDate now = LocalDate.now();

        Optional<User> userOptional = userRepository.findByEmail(scheduleRequestDto.getEmail());

        User user;

        if (userOptional.isEmpty()) {
            user = User.builder()
                    .name(scheduleRequestDto.getAuthor())
                    .email(scheduleRequestDto.getEmail())
                    .createdAt(now)
                    .updatedAt(now)
                    .build();

            user = userRepository.save(user);
        } else {
            user = userOptional.get();

            if (!user.getName().equals(scheduleRequestDto.getAuthor())) {
                throw new IllegalArgumentException("작성자와 이메일이 일치하지 않습니다.");
            }
        }

        Schedule schedule = Schedule.builder()
                .todo(scheduleRequestDto.getTodo())
                .userId(user.getId())
                .password(scheduleRequestDto.getPassword())
                .createdAt(now)
                .updatedAt(now)
                .build();

        Schedule savedSchedule = scheduleRepository.save(schedule);

        return ScheduleResponseDto.from(savedSchedule, user);
    }

    @Override
    @Transactional(readOnly = true)
    public ScheduleResponseDto getScheduleById(Long scheduleId) {

        Schedule schedule = scheduleRepository.findById(scheduleId);

        Optional<User> userOptional = userRepository.findById(schedule.getUserId());

        User user = userOptional.orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        return ScheduleResponseDto.from(schedule, user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScheduleResponseDto> getSchedulesByUser(Long userId, LocalDate updatedAt) {

        List<Schedule> schedules = scheduleRepository.findSchedulesByUserId(userId, updatedAt);

        return schedules.stream()
                .map(schedule -> {
                    Optional<User> userOptional = userRepository.findById(schedule.getUserId());
                    User user = userOptional.orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
                    return ScheduleResponseDto.from(schedule, user);
                })
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
                .userId(schedule.getUserId())
                .password(scheduleRequestDto.getPassword())
                .createdAt(schedule.getCreatedAt())
                .updatedAt(LocalDate.now())
                .build();

        Schedule savedSchedule = scheduleRepository.update(scheduleId, updatedSchedule);

        Optional<User> userOptional = userRepository.findById(schedule.getUserId());
        User user = userOptional.orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        User savedUser = userRepository.updateName(user.getId(), scheduleRequestDto.getAuthor(), user);

        return ScheduleResponseDto.from(savedSchedule, savedUser);
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
