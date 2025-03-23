package spring.basic.scheduler.challenge.service;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import spring.basic.scheduler.challenge.exception.ErrorCode;
import spring.basic.scheduler.challenge.exception.NoSuchScheduleException;
import spring.basic.scheduler.challenge.exception.PasswordValidationException;
import spring.basic.scheduler.challenge.model.dto.*;
import spring.basic.scheduler.challenge.model.entity.Schedule;
import spring.basic.scheduler.challenge.model.entity.Writer;
import spring.basic.scheduler.challenge.repository.SchedulerRepository;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * 전체적으로 읽기 전용 트랜잭션을 걸고 생성, 수정, 삭제만 @Transactional 적용
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SchedulerServiceImplV2 implements SchedulerService {

    private final SchedulerRepository schedulerRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public SchedulerCommonResponseDto saveSchedule(SchedulerCreateRequestDto createRequestDto) {

        Writer writer = createWriter(createRequestDto);                    // Writer 생성
        Long savedWriterId = schedulerRepository.saveWriter(writer);       // 작성자 정보부터 DB에 저장하고 key 반환

        Schedule schedule = createSchedule(createRequestDto, savedWriterId); // Schedule 생성
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

        // 찾을 ID가 없으면 예외 반환
        if (optionalFindSchedule.isEmpty()) {
            throw new NoSuchElementException("해당 id로 찾을 수 없습니다.");
        }

        return optionalFindSchedule.get();
    }


    @Override
    @Transactional
    public SchedulerCommonResponseDto updateSchedule(Long id, SchedulerUpdateRequestDto updateRequestDto) {

        passwordValid(id, updateRequestDto.getPassword());  // 패스워드 검증
        validUpdateSchedule(id, updateRequestDto);          // 동적으로 수정하고 수정할 대상을 못찾으면 예외 발생

        // 수정완료 되면 id값 반환
        return new SchedulerCommonResponseDto(id);
    }

    @Override
    @Transactional
    public void deleteSchedule(Long id, SchedulerDeleteRequestDto deleteDto) {
        passwordValid(id, deleteDto.getPassword()); // 패스워드 검증
        validDeleteSchedule(id);                    // 삭제할 대상을 못찾으면 예외 발생
    }

    /**
     * Writer 생성하는 메서드
     *
     * @param createRequestDto 생성을 위한 요청 정보
     * @return Write 객체
     */
    private Writer createWriter(SchedulerCreateRequestDto createRequestDto) {
        return Writer.builder()
                .name(createRequestDto.getName())
                .email(createRequestDto.getEmail())
                .create_date(LocalDateTime.now())
                .update_date(LocalDateTime.now())
                .build();
    }

    /**
     * Schedule 생성하는 메서드(패스워드 인코더 적용)
     *
     * @param createRequestDto 생성을 위한 요청 정보
     * @param savedWriterId 생성된 Writer의 id값
     * @return Schedule 객체
     */
    private Schedule createSchedule(SchedulerCreateRequestDto createRequestDto, Long savedWriterId) {
        String encodedPassword = passwordEncoder.encode(createRequestDto.getPassword());
        return Schedule.builder()
                .writerId(savedWriterId)    // 반환된 작성자 key 세팅
                .content(createRequestDto.getContent())
                .password(encodedPassword)
                .build();
    }

    /**
     * 동적 수정을 위한 로직을 메서드화
     *
     * @param id 수정할 대상의 id
     * @param updateRequestDto 수정 요청 값
     */
    private void validUpdateSchedule(Long id, SchedulerUpdateRequestDto updateRequestDto) {
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

        // 수정된 내역이 없으면 잘못된 요청으로 예외 발생
        if (updatedRow == 0) {
            throw new NoSuchScheduleException(ErrorCode.NOT_FOUND_SCHEDULE.getMessage());
        }
    }

    /**
     * 삭제할 대상을 못찾으면 예외를 발생하는 코드를 메서드화
     *
     * @param id 삭제할 대상
     */
    private void validDeleteSchedule(Long id) {
        int deleteRow = schedulerRepository.deleteSchedule(id);

        if (deleteRow == 0) {
            throw new NoSuchScheduleException(ErrorCode.NOT_FOUND_SCHEDULE.getMessage());
        }
    }

    /**
     * 패스워드 검증을 메서드화(패스워드 인코더 적용)
     *
     * @param id DB에서 조회할 id
     * @param password 요청한 비밀번호
     * @throws BadRequestException  비밀번호가 맞지 않으면 예외 발생
     */
    private void passwordValid(Long id, String password) {
        Optional<String> passwordById = schedulerRepository.findPasswordById(id);

        if (passwordById.isEmpty()) {
            throw new NoSuchScheduleException(ErrorCode.NOT_FOUND_SCHEDULE.getMessage());
        }

        if (!passwordEncoder.matches(password, passwordById.get())) {
            throw new PasswordValidationException(ErrorCode.UNAUTHORIZED_ACCESS.getMessage());
        }
    }

}
