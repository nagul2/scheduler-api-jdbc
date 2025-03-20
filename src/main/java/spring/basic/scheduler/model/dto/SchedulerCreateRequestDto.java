package spring.basic.scheduler.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SchedulerCreateRequestDto {

    private final String content;
    private final String name;
    private final String password;
}
