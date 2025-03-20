package spring.basic.scheduler.repository;

import spring.basic.scheduler.model.entity.Schedule;

public interface SchedulerRepository {
    Long saveContent(Schedule schedule);
}
