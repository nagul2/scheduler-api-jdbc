package spring.basic.scheduler.required.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.basic.scheduler.required.model.dto.SchedulerCommonRequestDto;
import spring.basic.scheduler.required.model.dto.SchedulerCommonResponseDto;
import spring.basic.scheduler.required.model.dto.SchedulerFindResponseDto;
import spring.basic.scheduler.required.model.dto.SchedulerSearchCond;
import spring.basic.scheduler.required.service.SchedulerService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/required/scheduler")
public class SchedulerController {

    private final SchedulerService schedulerService;

    @PostMapping
    public ResponseEntity<SchedulerCommonResponseDto> addSchedule(@RequestBody SchedulerCommonRequestDto commonRequestDto) {
        return new ResponseEntity<>(schedulerService.saveSchedule(commonRequestDto), HttpStatus.CREATED);
    }

    @GetMapping
    public List<SchedulerFindResponseDto> findSchedules(SchedulerSearchCond searchCond) {
        return schedulerService.findAllSchedules(searchCond);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SchedulerFindResponseDto> findSchedule(@PathVariable Long id) {
        return new ResponseEntity<>(schedulerService.findScheduleById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SchedulerCommonResponseDto> updateSchedule(@PathVariable Long id,
                                                                     @RequestBody SchedulerCommonRequestDto commonRequestDto) {
        return new ResponseEntity<>(schedulerService.updateSchedule(id, commonRequestDto), HttpStatus.OK);
    }

}
