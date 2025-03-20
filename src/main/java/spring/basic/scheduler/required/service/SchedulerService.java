package spring.basic.scheduler.required.service;

import spring.basic.scheduler.required.model.dto.*;

import java.util.List;

public interface SchedulerService {
    SchedulerCommonResponseDto saveSchedule(SchedulerCommonRequestDto commonRequestDto);

    List<SchedulerFindResponseDto> findAllSchedules(SchedulerSearchCond searchCond);

    SchedulerFindResponseDto findScheduleById(Long id);

    SchedulerCommonResponseDto updateSchedule(Long id, SchedulerCommonRequestDto commonRequestDto);

    void deleteSchedule(Long id, SchedulerDeleteRequestDto deleteDto);
}
