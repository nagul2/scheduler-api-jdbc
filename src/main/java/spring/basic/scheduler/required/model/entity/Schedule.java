package spring.basic.scheduler.required.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 일정 엔티티 - 필요한 필드만 생성할 수 있도록 빌더 패턴 사용
 */
@Builder
@Getter
@AllArgsConstructor
public class Schedule {
    private Long id;
    private String content;
    private String name;
    private String password;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

}
