package spring.basic.scheduler.required.service;

import spring.basic.scheduler.required.model.dto.SchedulerCreateRequestDto;
import spring.basic.scheduler.required.model.dto.SchedulerCommonResponseDto;
import spring.basic.scheduler.required.model.dto.SchedulerFindResponseDto;
import spring.basic.scheduler.required.model.dto.SchedulerSearchCond;

import java.util.List;

public interface SchedulerService {
    SchedulerCommonResponseDto saveContent(SchedulerCreateRequestDto CreateRequestDto);

    List<SchedulerFindResponseDto> findAllSchedules(SchedulerSearchCond searchCond);

    SchedulerFindResponseDto findScheduleById(Long id);
}
