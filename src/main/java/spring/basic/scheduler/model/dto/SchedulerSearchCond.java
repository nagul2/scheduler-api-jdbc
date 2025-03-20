package spring.basic.scheduler.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class SchedulerSearchCond {

    private LocalDate condDate;
    private String condName;

}
