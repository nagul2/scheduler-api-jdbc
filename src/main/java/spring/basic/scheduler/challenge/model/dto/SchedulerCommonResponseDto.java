package spring.basic.scheduler.challenge.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 일정 생성, 수정 시 사용되는 공통 응답 DTO
 */

@Getter
@AllArgsConstructor
public class SchedulerCommonResponseDto {
    private Long id;
}
