package spring.basic.scheduler.challenge.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 비밀번호 검증 실패(인증 실패)시 예외 응답 API
     *
     * @param e 비밀번호 검증 실패 예외
     * @return 메시지, 401 상태코드
     */
    @ExceptionHandler
    public ResponseEntity<ErrorDto> passwordException(PasswordValidationException e) {
        log.error("[passwordException] ex: ", e);
        ErrorDto errorDto = new ErrorDto(ErrorCode.UNAUTHORIZED_ACCESS.getCode(), e.getMessage());
        return new ResponseEntity<>(errorDto, ErrorCode.UNAUTHORIZED_ACCESS.getHttpStatus());
    }

    /**
     * 일정 찾기 실패시 응답 API
     *
     * @param e 일정을 못찾는 예외
     * @return 메시지, 404 상태코드
     */
    @ExceptionHandler
    public ResponseEntity<ErrorDto> noSuchScheduleException(NoSuchScheduleException e) {
        log.error("[noSuchScheduleException] ex: ", e);
        ErrorDto errorDto = new ErrorDto(ErrorCode.NOT_FOUND_SCHEDULE.getCode(), e.getMessage());
        return new ResponseEntity<>(errorDto, ErrorCode.NOT_FOUND_SCHEDULE.getHttpStatus());
    }

    /**
     * 필수 입력값 검증 실패시 예외 응답 API
     *
     * @param e Validated 검증 실패 시 발생하는 예외
     * @return 검증 실패한 필드와 메시지, 400 상태코드
     */
    @ExceptionHandler
    public ResponseEntity<ErrorDto> inputValidException(MethodArgumentNotValidException e) {
        log.error("[inputValidException] ex: ", e);

        // @Validated 에서 발생한 에러만 꺼내서 스트림을 통해 에러의 필드와 메시지만 꺼내는 코드, GPT 도움 받았음
        Map<String, String> errors = e.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage,
                        (msg1, msg2) -> msg1
                ));

        ErrorDto errorDto = new ErrorDto(ErrorCode.VALID_BAD_REQUEST.getCode(), errors.toString());
        return new ResponseEntity<>(errorDto, ErrorCode.VALID_BAD_REQUEST.getHttpStatus());
    }
}
