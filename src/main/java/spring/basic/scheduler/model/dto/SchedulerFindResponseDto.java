package spring.basic.scheduler.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SchedulerFindResponseDto {

    private Long id;
    private String content;
    private String name;
    private LocalDate updateDate;

}
