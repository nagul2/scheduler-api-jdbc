package spring.basic.scheduler.challenge.exception;

/**
 * 비밀번호가 틀리면 발생하는 RuntimeException
 * BadRequestException 을 사용해도 되지만 체크 예외라서 모든 계층에서 throws로 던져야하기 때문에
 * 별도로 비밀번호 검증 예외를 RuntimeException 으로 만들어서 사용했음
 */
public class PasswordValidationException extends RuntimeException {
    public PasswordValidationException(String message) {
        super(message);
    }
}
