package spring.basic.scheduler.challenge.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 삭제 요청 시 사용되는 요청 DTO
 */
@Getter
@AllArgsConstructor
public class SchedulerDeleteRequestDto {

    @NotNull(message = "비밀번호는 필수 입력값 입니다.")
    @NotBlank(message = "비밀번호는 필수 입력값 입니다.")
    private String password;
}
