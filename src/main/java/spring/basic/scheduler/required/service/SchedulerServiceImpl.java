package spring.basic.scheduler.required.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import spring.basic.scheduler.required.model.dto.*;
import spring.basic.scheduler.required.model.entity.Schedule;
import spring.basic.scheduler.required.repository.SchedulerRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 전체적으로 읽기 전용 트랜잭션을 걸고 생성, 수정, 삭제만 @Transactional 적용
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SchedulerServiceImpl implements SchedulerService {

    private final SchedulerRepository schedulerRepository;

    @Override
    @Transactional
    public SchedulerCommonResponseDto saveSchedule(SchedulerCommonRequestDto commonRequestDto) {

        Schedule schedule = Schedule.builder()
                .content(commonRequestDto.getContent())
                .name(commonRequestDto.getName())
                .password(commonRequestDto.getPassword())
                .createDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        // 생성된 일정의 ID값 반환
        Long savedScheduleId = schedulerRepository.saveSchedule(schedule);
        return new SchedulerCommonResponseDto(savedScheduleId);
    }

    @Override
    public List<SchedulerFindResponseDto> findAllSchedules(SchedulerSearchCond searchCond) {
        return schedulerRepository.findAllSchedules(searchCond);    // 조회한 일정을 리스트로 반환
    }

    @Override
    public SchedulerFindResponseDto findScheduleById(Long id) {
        Optional<SchedulerFindResponseDto> optionalFindSchedule = schedulerRepository.findScheduleById(id);

        // 필수 예외 처리 없음 -> 도전에서 예외처리 강화하면서 상태코드 반환 예정
        if (optionalFindSchedule.isEmpty()) {
            return null;
        }

        return optionalFindSchedule.get();  // 조회한 일정을 반환, 검증로직을 거쳤으므로 .get()으로 바로 반환
    }

    @Override
    @Transactional
    public SchedulerCommonResponseDto updateSchedule(Long id, SchedulerCommonRequestDto commonRequestDto) {

        // 패스워드 검증
        String findPassword = schedulerRepository.findPasswordById(id);

        // 필수 구현에서는 별도 예외 처리 없이 비밀번호를 못찾거나 비밀번호가 안맞으면 null을 반환함 -> 도전에서 예외처리하면서 상태코드 반환 예정
        if (!StringUtils.hasText(findPassword) || !findPassword.equals(commonRequestDto.getPassword())) {
            return null;
        }

        int updatedRow = schedulerRepository.updateSchedule(id, commonRequestDto.getContent(), commonRequestDto.getName());

        // 필수 구현에서는 일단 수정된 값이 없으면 null로 반환
        if (updatedRow == 0) {
            return null;
        }
        // 수정완료 되면 id값 반환
        return new SchedulerCommonResponseDto(id);
    }

    @Override
    @Transactional
    public void deleteSchedule(Long id, SchedulerDeleteRequestDto deleteDto) {
        // 패스워드 검증
        String findPassword = schedulerRepository.findPasswordById(id);

        // 필수 구현에서는 별도 예외 처리 없이 비밀번호를 못찾거나 비밀번호가 안맞으면 그냥 리턴 -> 도전에서 예외처리하면서 상태코드 반환 예정
        if (!StringUtils.hasText(findPassword) || !findPassword.equals(deleteDto.getPassword())) {
            return ;
        }
        schedulerRepository.deleteSchedule(id);
    }

}
