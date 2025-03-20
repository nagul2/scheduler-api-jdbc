package spring.basic.scheduler.required.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class SchedulerSearchCond {
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate condDate;
    private String condName;

}
