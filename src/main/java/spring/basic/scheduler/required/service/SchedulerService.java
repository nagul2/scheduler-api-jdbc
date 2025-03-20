package spring.basic.scheduler.required.service;

import spring.basic.scheduler.required.model.dto.SchedulerCommonResponseDto;
import spring.basic.scheduler.required.model.dto.SchedulerCommonRequestDto;
import spring.basic.scheduler.required.model.dto.SchedulerFindResponseDto;
import spring.basic.scheduler.required.model.dto.SchedulerSearchCond;

import java.util.List;

public interface SchedulerService {
    SchedulerCommonResponseDto saveSchedule(SchedulerCommonRequestDto commonRequestDto);

    List<SchedulerFindResponseDto> findAllSchedules(SchedulerSearchCond searchCond);

    SchedulerFindResponseDto findScheduleById(Long id);

    SchedulerCommonResponseDto updateSchedule(Long id, SchedulerCommonRequestDto commonRequestDto);
}
