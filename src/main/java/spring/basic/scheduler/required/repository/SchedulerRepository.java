package spring.basic.scheduler.required.repository;

import spring.basic.scheduler.required.model.dto.SchedulerFindResponseDto;
import spring.basic.scheduler.required.model.dto.SchedulerSearchCond;
import spring.basic.scheduler.required.model.entity.Schedule;

import java.util.List;
import java.util.Optional;

public interface SchedulerRepository {
    Long saveContent(Schedule schedule);

    List<SchedulerFindResponseDto> findAllSchedules(SchedulerSearchCond searchCond);

    Optional<SchedulerFindResponseDto> findScheduleById(Long id);

}
