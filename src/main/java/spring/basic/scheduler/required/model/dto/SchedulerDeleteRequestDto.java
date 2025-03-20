package spring.basic.scheduler.required.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 삭제 요청 시 사용되는 요청 DTO
 */
@Getter
@AllArgsConstructor
public class SchedulerDeleteRequestDto {
    private String password;
}
