package spring.basic.scheduler.challenge.model.entity;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 작성자 엔티티 - 빌더 패턴 사용
 */
@Builder
@Getter
public class Writer {

    private Long id;
    private String name;
    private String email;
    private LocalDateTime create_date;
    private LocalDateTime update_date;
}
