package spring.basic.scheduler.required.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import spring.basic.scheduler.required.model.dto.SchedulerFindResponseDto;
import spring.basic.scheduler.required.model.dto.SchedulerSearchCond;
import spring.basic.scheduler.required.model.entity.Schedule;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class SchedulerRepositoryImpl implements SchedulerRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public SchedulerRepositoryImpl(DataSource dataSource) {
        // 바인딩 순서로 쿼리하면 버그가 생길 수 있으므로 파라미터 이름으로 쿼리를 할 수 있는 JdbcTemplate
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)  // Insert 편의 기능 활용
                .withTableName("schedule")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Long saveSchedule(Schedule schedule) {
        SqlParameterSource param = new BeanPropertySqlParameterSource(schedule);
        Number key = jdbcInsert.executeAndReturnKey(param);
        return key.longValue(); // 생성한 key 값을 long 타입으로 변환해서 반환
    }


    @Override
    public List<SchedulerFindResponseDto> findAllSchedules(SchedulerSearchCond searchCond) {
        LocalDate condDate = searchCond.getCondDate();  // 날짜 검색 조건
        String condName = searchCond.getCondName();     // 이름 검색 조

        SqlParameterSource param = new BeanPropertySqlParameterSource(searchCond);

        // 동적 쿼리 시작
        String query = "select id, content, name, update_date from schedule";

        // 동적 쿼리 작성하기
        // 날짜가 null이 아니거나 이름이 null, 길이 0, 공백 문자만으로 구성되어있지 않으면 -> 즉 동적 쿼리 조건이 있으면 where 붙이기
        if (condDate != null || StringUtils.hasText(condName)) {
            query += " where";
        }

        boolean andFlag = false;    // and 조건 붙이기 위한 플래그
        if (condDate != null) {
            // DB의 update_date 컬럼의 타입이 시, 분, 초가 있으므로 날짜 조건만 맞추기 위해 like 문법 사용
            query += " update_date like concat(:condDate, '%')";
            andFlag = true;         // and 플래그 true 설정
        }

        if (StringUtils.hasText(condName)) {
            // 날짜 조건이 null이 아니라서 쿼리가 추가 되면 쿼리에 and 추가
            if (andFlag) {
                query += " and";
            }
            query += " name = :condName";   // 작성자 이름 같은 일정 조회
        }

        query += " order by update_date desc";  // 마지막 수정일 기준 내림차순 조회

        return jdbcTemplate.query(query, param, scheduleRowMapper());
    }

    @Override
    public Optional<SchedulerFindResponseDto> findScheduleById(Long id) {
        String query = "select id, content, name, update_date from schedule where id = :id";

        Map<String, Long> param = Map.of("id", id);
        List<SchedulerFindResponseDto> findScheduleDto = jdbcTemplate.query(query, param, scheduleRowMapper());

        // Optional 반환
        return findScheduleDto.stream().findAny();
    }

    @Override
    public String findPasswordById(Long id) {
        String query = "select password from schedule where id = :id";
        Map<String, Long> param = Map.of("id", id);

        // 단건을 조회하는 queryForObject는 못찾으면 EmptyResultDataAccessException이 발생한다고 함
        try {
            return jdbcTemplate.queryForObject(query, param, String.class);
        } catch(EmptyResultDataAccessException e) {
            return null;    // 못찾으면 null
        }
    }


    @Override
    public int updateSchedule(Long id, String content, String name) {
        // 업데이트 후 수정일을 변경한 시간으로 업데이트
        String query = "update schedule set content = :content, name = :name, update_date = now() where id = :id";

        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("id", id)
                .addValue("content", content)
                .addValue("name", name);

        return jdbcTemplate.update(query, param);
    }

    @Override
    public int deleteSchedule(Long id) {
        String query = "delete from schedule where id = :id";
        SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
        return jdbcTemplate.update(query, param);
    }

    /**
     * 결과를 객체로 매핑하기 위한 RowMapper
     * BeanPropertyRowMapper: 자바빈 규약에 맞춰서 데이터를 변환함, DB의 스네이크 표기법을 카멜로 자동 변환해줌
     *
     * @return SchedulerFindResponseDto 필드를 자바빈 규약에 맞춰서 변환한 RowMapper 반환
     */
    private RowMapper<SchedulerFindResponseDto> scheduleRowMapper() {
        return BeanPropertyRowMapper.newInstance(SchedulerFindResponseDto.class);
    }
}
