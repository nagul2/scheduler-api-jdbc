package spring.basic.scheduler.challenge.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 일정 생성, 수정 시 사용되는 공통 요청 DTO
 */
@Getter
@AllArgsConstructor
public class SchedulerUpdateRequestDto {

    @Size(max = 200)
    @NotNull(message = "할일은 필수 입력값 입니다.")
    @NotBlank(message = "할일은 필수 입력값 입니다.")
    private final String content;

    @NotNull(message = "사용자이름은 필수 입력값 입니다.")
    @NotBlank(message = "사용자이름은 필수 입력값 입니다.")
    private final String name;

    @NotNull(message = "비밀번호는 필수 입력값 입니다.")
    @NotBlank(message = "비밀번호는 필수 입력값 입니다.")
    private final String password;
}
