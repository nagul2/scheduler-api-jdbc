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

        Long savedScheduleId = schedulerRepository.saveSchedule(schedule);
        return new SchedulerCommonResponseDto(savedScheduleId);
    }

    @Override
    public List<SchedulerFindResponseDto> findAllSchedules(SchedulerSearchCond searchCond) {
        return schedulerRepository.findAllSchedules(searchCond);
    }

    @Override
    public SchedulerFindResponseDto findScheduleById(Long id) {
        Optional<SchedulerFindResponseDto> optionalFindSchedule = schedulerRepository.findScheduleById(id);

        // 필수 예외 처리 없음 -> 도전에서 예외처리 강화하면서 상태코드 반환 예정
        if (optionalFindSchedule.isEmpty()) {
            return null;
        }

        return optionalFindSchedule.get();
    }

    @Override
    @Transactional
    public SchedulerCommonResponseDto updateSchedule(Long id, SchedulerCommonRequestDto commonRequestDto) {
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
        String findPassword = schedulerRepository.findPasswordById(id);

        // 필수 구현에서는 별도 예외 처리 없이 비밀번호를 못찾거나 비밀번호가 안맞으면 그냥 리턴 -> 도전에서 예외처리하면서 상태코드 반환 예정
        if (!StringUtils.hasText(findPassword) || !findPassword.equals(deleteDto.getPassword())) {
            return ;
        }
        schedulerRepository.deleteSchedule(id);
    }

}
