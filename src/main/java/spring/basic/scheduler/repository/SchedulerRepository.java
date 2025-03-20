package spring.basic.scheduler.repository;

import spring.basic.scheduler.model.dto.SchedulerFindResponseDto;
import spring.basic.scheduler.model.dto.SchedulerSearchCond;
import spring.basic.scheduler.model.entity.Schedule;

import java.util.List;

public interface SchedulerRepository {
    Long saveContent(Schedule schedule);

    List<SchedulerFindResponseDto> findAllSchedules(SchedulerSearchCond searchCond);
}
