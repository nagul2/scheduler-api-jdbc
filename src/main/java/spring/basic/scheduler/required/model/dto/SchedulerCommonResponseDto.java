package spring.basic.scheduler.required.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 일정 생성, 수정 시 사용되는 공통 응답 DTO
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SchedulerCommonResponseDto {
    private Long id;
}
