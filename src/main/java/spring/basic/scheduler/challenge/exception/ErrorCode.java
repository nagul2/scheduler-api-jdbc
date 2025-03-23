package spring.basic.scheduler.challenge.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // password
    UNAUTHORIZED_ACCESS("401", HttpStatus.UNAUTHORIZED, "비밀번호가 틀립니다. 다시 입력해 주세요"),

    // noSuch
    NOT_FOUND_SCHEDULE("404", HttpStatus.NOT_FOUND, "일정이 존재하지 않습니다."),
    ;

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

}
