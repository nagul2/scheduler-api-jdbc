package spring.basic.scheduler.challenge.exception;

/**
 * 일정을 찾지 못했을 때 발생하는 예외
 */
public class NoSuchScheduleException extends RuntimeException {
    public NoSuchScheduleException(String message) {
        super(message);
    }
}
