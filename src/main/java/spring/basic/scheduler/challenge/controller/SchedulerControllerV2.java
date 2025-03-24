package spring.basic.scheduler.challenge.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.basic.scheduler.challenge.model.dto.*;
import spring.basic.scheduler.challenge.service.SchedulerService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/challenge/scheduler")
public class SchedulerControllerV2 {

    private final SchedulerService schedulerService;

    /**
     * 일정 등록(생성) API
     *
     * @param createRequestDto 일정 등록/수정 시 공통으로 사용하는 DTO
     * @return 생성된 일정의 ID와 응답코드를 반환
     */
    @PostMapping
    public ResponseEntity<SchedulerCommonResponseDto> addSchedule(@RequestBody SchedulerCreateRequestDto createRequestDto) {
        return new ResponseEntity<>(schedulerService.saveSchedule(createRequestDto), HttpStatus.CREATED);
    }

    /**
     * 일정 전체 조회 API - 검색 적용
     * 검색 조건이 모두 있거나, 하나만 있거나, 없어도 동작함
     *
     * @param searchCond 검색 조건 DTO
     * @return 조회된 일정을 리스트로 반환하고 응답코드를 반환
     */
    @GetMapping
    public ResponseEntity<List<SchedulerFindResponseDto>> findSchedules(SchedulerSearchCond searchCond) {
        return new ResponseEntity<>(schedulerService.findAllSchedules(searchCond), HttpStatus.OK);
    }

    /**
     * 일정 단건 조회 API
     *
     * @param id 조회 할 일정의 ID
     * @return ID로 조회한 일정과 응답코드를 반환
     */
    @GetMapping("/{id}")
    public ResponseEntity<SchedulerFindResponseDto> findSchedule(@PathVariable Long id) {
        return new ResponseEntity<>(schedulerService.findScheduleById(id), HttpStatus.OK);
    }

    /**
     * 일정 수정 API
     *
     * @param id 수정 할 일정의 ID
     * @param updateRequestDto 일정 등록/수정 시 공통으로 사용하는 DTO
     * @return 수정된 일정의 ID와 응답코드 반환
     */
    @PutMapping("/{id}")
    public ResponseEntity<SchedulerCommonResponseDto> updateSchedule(@PathVariable Long id,
                                                                     @RequestBody SchedulerUpdateRequestDto updateRequestDto) {
        return new ResponseEntity<>(schedulerService.updateSchedule(id, updateRequestDto), HttpStatus.OK);
    }

    /**
     * 일정 삭제 API
     *
     * @param id 삭제 할 일정의 ID
     * @param deleteDto 일정 삭제 시 사용하는 DTO
     * @return 응답코드만 반환
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id, @RequestBody SchedulerDeleteRequestDto deleteDto) {
        schedulerService.deleteSchedule(id, deleteDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
