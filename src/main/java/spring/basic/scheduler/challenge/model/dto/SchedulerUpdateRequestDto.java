package spring.basic.scheduler.challenge.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 일정 생성, 수정 시 사용되는 공통 요청 DTO
 * 수정은 이름, 할일 둘중 하나만 수정 가능하도록 NotNull, NotBlank 검증 제거
 */
@Getter
@AllArgsConstructor
public class SchedulerUpdateRequestDto {

    @Size(max = 200)
    private final String content;   // 수정은 필수 X

    private final String name;      // 수정은 필수 X

    @NotNull(message = "비밀번호는 필수 입력값 입니다.")
    @NotBlank(message = "비밀번호는 필수 입력값 입니다.")
    private final String password;
}
