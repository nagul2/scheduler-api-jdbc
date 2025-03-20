package spring.basic.scheduler.required.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * 조회 시 사용되는 응답 DTO
 */
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
