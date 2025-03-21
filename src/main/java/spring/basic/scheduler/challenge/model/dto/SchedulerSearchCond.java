package spring.basic.scheduler.challenge.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * 조회 시 사용되는 조건 DTO
 */
@Getter
@AllArgsConstructor
public class SchedulerSearchCond {

    // 요구 조건에 따라 날짜 포맷을 연-월-일 정보만 가지고 있음
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate condDate;
    private String condName;

}
