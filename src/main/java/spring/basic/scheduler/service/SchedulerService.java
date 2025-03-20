package spring.basic.scheduler.service;

import spring.basic.scheduler.model.dto.SchedulerCreateRequestDto;
import spring.basic.scheduler.model.dto.SchedulerCommonResponseDto;

public interface SchedulerService {
    SchedulerCommonResponseDto saveContent(SchedulerCreateRequestDto CreateRequestDto);
}
