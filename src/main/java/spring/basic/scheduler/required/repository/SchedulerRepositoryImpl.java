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
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("schedule")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Long saveSchedule(Schedule schedule) {
        SqlParameterSource param = new BeanPropertySqlParameterSource(schedule);
        Number key = jdbcInsert.executeAndReturnKey(param);
        return key.longValue();
    }

    @Override
    public List<SchedulerFindResponseDto> findAllSchedules(SchedulerSearchCond searchCond) {
        LocalDate condDate = searchCond.getCondDate();
        String condName = searchCond.getCondName();

        SqlParameterSource param = new BeanPropertySqlParameterSource(searchCond);

        String query = "select id, content, name, update_date from schedule";

        // 동적 쿼리 작성하기
        // 날짜가 null이 아니거나 이름이 null, 길이 0, 공백 문자만으로 구성되어있지 않으면!
        if (condDate != null || StringUtils.hasText(condName)) {
            query += " where";
        }

        boolean andFlag = false;
        if (condDate != null) {
            query += " update_date like concat(:condDate, '%')";
            andFlag = true;
        }

        if (StringUtils.hasText(condName)) {
            if (andFlag) {
                query += " and";
            }
            query += " name = :condName";
        }

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

        // 단건을 조회하면 queryForObject는 못찾으면 EmptyResultDataAccessException이 발생한다고 함
        try {
            return jdbcTemplate.queryForObject(query, param, String.class);
        } catch(EmptyResultDataAccessException e) {
            return null;    // 못찾으면 null
        }
    }

    @Override
    public int updateSchedule(Long id, String content, String name) {
        String query = "update schedule set content = :content, name = :name, update_date = now() where id = :id";

        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("id", id)
                .addValue("content", content)
                .addValue("name", name);

        return jdbcTemplate.update(query, param);
    }

    private RowMapper<SchedulerFindResponseDto> scheduleRowMapper() {
        return BeanPropertyRowMapper.newInstance(SchedulerFindResponseDto.class);
    }
}
