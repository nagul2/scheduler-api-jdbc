package spring.basic.scheduler.challenge.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 일정 생성, 수정 시 사용되는 공통 요청 DTO
 */
@Getter
@AllArgsConstructor
public class SchedulerCreateRequestDto {

    private final String content;
    private final String name;
    private final String email;
    private final String password;
}
