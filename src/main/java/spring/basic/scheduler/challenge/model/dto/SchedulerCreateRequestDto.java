package spring.basic.scheduler.challenge.model.dto;

import jakarta.validation.constraints.Email;
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
public class SchedulerCreateRequestDto {

    @Size(max = 200, message = "할일은 200자 미만이여야 합니다.")
    @NotNull(message = "할일은 필수 입력값 입니다.")
    @NotBlank(message = "할일은 공백만 입력할 수 없습니다.")
    private final String content;

    @NotNull(message = "사용자 이름은 필수 입력값 입니다.")
    @NotBlank(message = "사용자 이름은 공백만 입력할 수 없습니다.")
    private final String name;

    @Email
    @NotNull(message = "이메일은 필수 입력값 입니다.")
    @NotBlank(message = "이메일은 공백만 입력할 수 없습니다.")
    private final String email;

    @NotNull(message = "비밀번호는 필수 입력값 입니다.")
    @NotBlank(message = "비밀번호는 공백만 입력할 수 없습니다.")
    private final String password;
}
