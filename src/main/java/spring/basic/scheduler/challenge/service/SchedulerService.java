package spring.basic.scheduler.challenge.service;

import spring.basic.scheduler.challenge.model.dto.*;

import java.util.List;

public interface SchedulerService {
    SchedulerCommonResponseDto saveSchedule(SchedulerCreateRequestDto createRequestDto);

    List<SchedulerFindResponseDto> findAllSchedules(SchedulerSearchCond searchCond);

    SchedulerFindResponseDto findScheduleById(Long id);

    SchedulerCommonResponseDto updateSchedule(Long id, SchedulerUpdateRequestDto updateRequestDto);

    void deleteSchedule(Long id, SchedulerDeleteRequestDto deleteDto);
}
