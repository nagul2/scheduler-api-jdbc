package spring.basic.scheduler.required.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
public class Schedule {
    private Long id;
    private String content;
    private String name;
    private String password;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

}
