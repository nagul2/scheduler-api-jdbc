package spring.basic.scheduler.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.basic.scheduler.model.dto.SchedulerCreateRequestDto;
import spring.basic.scheduler.model.dto.SchedulerCommonResponseDto;
import spring.basic.scheduler.model.dto.SchedulerFindResponseDto;
import spring.basic.scheduler.model.dto.SchedulerSearchCond;
import spring.basic.scheduler.model.entity.Schedule;
import spring.basic.scheduler.repository.SchedulerRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    @Override
    public List<SchedulerFindResponseDto> findAllSchedules(SchedulerSearchCond searchCond) {
        return schedulerRepository.findAllSchedules(searchCond);
    }

    @Override
    public SchedulerFindResponseDto findScheduleById(Long id) {
        Optional<SchedulerFindResponseDto> optionalFindSchedule = schedulerRepository.findScheduleById(id);

        // LV1은 예외 처리 없음
        if (optionalFindSchedule.isEmpty()) {
            return null;
        }

        return optionalFindSchedule.get();
    }
}
