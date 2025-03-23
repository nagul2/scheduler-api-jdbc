package spring.basic.scheduler.challenge.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import spring.basic.scheduler.challenge.model.dto.*;
import spring.basic.scheduler.challenge.model.entity.Schedule;
import spring.basic.scheduler.challenge.model.entity.Writer;
import spring.basic.scheduler.challenge.repository.SchedulerRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 전체적으로 읽기 전용 트랜잭션을 걸고 생성, 수정, 삭제만 @Transactional 적용
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SchedulerServiceImplV2 implements SchedulerService {

    private final SchedulerRepository schedulerRepository;

    @Override
    @Transactional
    public SchedulerCommonResponseDto saveSchedule(SchedulerCreateRequestDto createRequestDto) {

        Writer writer = Writer.builder()
                .name(createRequestDto.getName())
                .email(createRequestDto.getEmail())
                .create_date(LocalDateTime.now())
                .update_date(LocalDateTime.now())
                .build();

        Long savedWriterId = schedulerRepository.saveWriter(writer);       // 작성자 정보부터 DB에 저장하고 key 반환

        Schedule schedule = Schedule.builder()
                .writerId(savedWriterId)    // 반환된 작성자 key 세팅
                .content(createRequestDto.getContent())
                .password(createRequestDto.getPassword())
                .build();

        Long savedScheduleId = schedulerRepository.saveSchedule(schedule);  // 일정 DB 저장하고 Key 반환

        return new SchedulerCommonResponseDto(savedScheduleId);
    }

    @Override
    public Page<SchedulerFindResponseDto> findAllSchedules(SchedulerSearchCond searchCond, Pageable pageable) {
        return schedulerRepository.findAllSchedules(searchCond, pageable);    // 조회한 일정을 리스트로 반환
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
    public SchedulerCommonResponseDto updateSchedule(Long id, SchedulerUpdateRequestDto updateRequestDto) {

        // 패스워드 검증
        String findPassword = schedulerRepository.findPasswordById(id);

        // 필수 구현에서는 별도 예외 처리 없이 비밀번호를 못찾거나 비밀번호가 안맞으면 null을 반환함 -> 도전에서 예외처리하면서 상태코드 반환 예정
        if (!StringUtils.hasText(findPassword) || !findPassword.equals(updateRequestDto.getPassword())) {
            return null;
        }

        int updatedRow;

        if (StringUtils.hasText(updateRequestDto.getContent()) && !StringUtils.hasText(updateRequestDto.getName())) {
            // 일정만 수정
            updatedRow = schedulerRepository.updateScheduleContent(id, updateRequestDto.getContent());

        } else if (StringUtils.hasText(updateRequestDto.getName()) && !StringUtils.hasText(updateRequestDto.getContent())) {
            // 이름만 수정
            updatedRow = schedulerRepository.updateWriterName(id, updateRequestDto.getName());

        } else {
            // 전체 수정
            updatedRow = schedulerRepository.updateScheduleContentWithWriterName(id, updateRequestDto.getContent(), updateRequestDto.getName());
        }

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
            return;
        }
        schedulerRepository.deleteSchedule(id);
    }

}
