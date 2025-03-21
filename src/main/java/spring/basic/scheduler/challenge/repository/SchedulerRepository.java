package spring.basic.scheduler.challenge.repository;

import spring.basic.scheduler.challenge.model.dto.SchedulerFindResponseDto;
import spring.basic.scheduler.challenge.model.dto.SchedulerSearchCond;
import spring.basic.scheduler.challenge.model.entity.Schedule;
import spring.basic.scheduler.challenge.model.entity.Writer;

import java.util.List;
import java.util.Optional;

public interface SchedulerRepository {
    Long saveSchedule(Schedule schedule);
    Long saveWriter(Writer writer);

    List<SchedulerFindResponseDto> findAllSchedules(SchedulerSearchCond searchCond);

    Optional<SchedulerFindResponseDto> findScheduleById(Long id);

    String findPasswordById(Long id);

    int updateScheduleContent(Long id, String content);

    int updateWriterName(Long id, String name);

    int updateScheduleContentWithWriterName(Long id, String content, String name);

    int deleteSchedule(Long id);

}
