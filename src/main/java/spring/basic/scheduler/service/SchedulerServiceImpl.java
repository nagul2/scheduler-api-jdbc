package spring.basic.scheduler.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.basic.scheduler.model.dto.SchedulerCreateRequestDto;
import spring.basic.scheduler.model.dto.SchedulerCommonResponseDto;
import spring.basic.scheduler.model.entity.Schedule;
import spring.basic.scheduler.repository.SchedulerRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SchedulerServiceImpl implements SchedulerService {

    private final SchedulerRepository schedulerRepository;

    @Override
    @Transactional
    public SchedulerCommonResponseDto saveContent(SchedulerCreateRequestDto createRequestDto) {

        Schedule schedule = Schedule.builder()
                .content(createRequestDto.getContent())
                .name(createRequestDto.getName())
                .password(createRequestDto.getPassword())
                .createDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        Long savedContentId = schedulerRepository.saveContent(schedule);
        return new SchedulerCommonResponseDto(savedContentId);
    }
}
