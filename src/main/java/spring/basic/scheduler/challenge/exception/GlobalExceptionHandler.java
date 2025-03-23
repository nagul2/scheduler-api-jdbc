package spring.basic.scheduler.challenge.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorDto> passwordException(PasswordValidationException e) {
        log.error("[passwordException] ex: ", e);
        ErrorDto errorDto = new ErrorDto(ErrorCode.UNAUTHORIZED_ACCESS.getCode(), e.getMessage());
        return new ResponseEntity<>(errorDto, ErrorCode.UNAUTHORIZED_ACCESS.getHttpStatus());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorDto> noSuchScheduleException(NoSuchScheduleException e) {
        log.error("[noSuchScheduleException] ex: ", e);
        ErrorDto errorDto = new ErrorDto(ErrorCode.NOT_FOUND_SCHEDULE.getCode(), e.getMessage());
        return new ResponseEntity<>(errorDto, ErrorCode.NOT_FOUND_SCHEDULE.getHttpStatus());
    }
}
