package spring.basic.scheduler.challenge.model.entity;

import lombok.Builder;
import lombok.Getter;

/**
 * 일정 엔티티 - 필요한 필드만 생성할 수 있도록 빌더 패턴 사용
 */
@Builder
@Getter
public class Schedule {

    private Long id;
    private Long writerId;  // 작성자 아이디 -> 외래키
    private String content;
    private String password;

}


