package spring.basic.scheduler.service;

import spring.basic.scheduler.model.dto.SchedulerCreateRequestDto;
import spring.basic.scheduler.model.dto.SchedulerCommonResponseDto;
import spring.basic.scheduler.model.dto.SchedulerFindResponseDto;
import spring.basic.scheduler.model.dto.SchedulerSearchCond;

import java.util.List;

public interface SchedulerService {
    SchedulerCommonResponseDto saveContent(SchedulerCreateRequestDto CreateRequestDto);

    List<SchedulerFindResponseDto> findAllSchedules(SchedulerSearchCond searchCond);
}
