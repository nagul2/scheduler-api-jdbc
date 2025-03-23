package spring.basic.scheduler.challenge.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import spring.basic.scheduler.challenge.model.dto.*;

public interface SchedulerService {
    SchedulerCommonResponseDto saveSchedule(SchedulerCreateRequestDto createRequestDto);

    Page<SchedulerFindResponseDto> findAllSchedules(SchedulerSearchCond searchCond, Pageable pageable);

    SchedulerFindResponseDto findScheduleById(Long id);

    SchedulerCommonResponseDto updateSchedule(Long id, SchedulerUpdateRequestDto updateRequestDto);

    void deleteSchedule(Long id, SchedulerDeleteRequestDto deleteDto);
}
