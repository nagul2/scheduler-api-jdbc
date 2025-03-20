package spring.basic.scheduler.required.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class SchedulerSearchCond {

    private LocalDate condDate;
    private String condName;

}
