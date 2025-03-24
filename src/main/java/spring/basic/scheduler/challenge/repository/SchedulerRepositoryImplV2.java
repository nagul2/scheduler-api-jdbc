package spring.basic.scheduler.challenge.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import spring.basic.scheduler.challenge.model.dto.SchedulerFindResponseDto;
import spring.basic.scheduler.challenge.model.dto.SchedulerSearchCond;
import spring.basic.scheduler.challenge.model.entity.Schedule;
import spring.basic.scheduler.challenge.model.entity.Writer;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class SchedulerRepositoryImplV2 implements SchedulerRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert schedulerJdbcInsert;
    private final SimpleJdbcInsert writerJdbcInsert;

    public SchedulerRepositoryImplV2(DataSource dataSource) {
        // 바인딩 순서로 쿼리하면 버그가 생길 수 있으므로 파라미터 이름으로 쿼리를 할 수 있는 JdbcTemplate
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.schedulerJdbcInsert = new SimpleJdbcInsert(dataSource)  // Insert 편의 기능 활용
                .withTableName("schedule")
                .usingGeneratedKeyColumns("id");

        this.writerJdbcInsert = new SimpleJdbcInsert(dataSource)  // Insert 편의 기능 활용
                .withTableName("writer")
                .usingGeneratedKeyColumns("id");
    }

    /**
     * 작성자 정보를 DB에 저장하는 메서드
     *
     * @param writer 작성자 정보
     * @return 테이블에 저장된 작성자 key
     */
    @Override
    public Long saveWriter(Writer writer) {
        SqlParameterSource writerParam = new BeanPropertySqlParameterSource(writer);
        Number writerKey = writerJdbcInsert.executeAndReturnKey(writerParam);
        return writerKey.longValue();
    }

    /**
     * 일정 정보를 DB에 저장하는 메서드
     *
     * @param schedule 일정 정보e
     * @return 테이블에 저장된 일정 key
     */
    @Override
    public Long saveSchedule(Schedule schedule) {
        SqlParameterSource scheduleParam = new BeanPropertySqlParameterSource(schedule);
        Number schedulerKey = schedulerJdbcInsert.executeAndReturnKey(scheduleParam);
        return schedulerKey.longValue(); // 생성한 key 값을 long 타입으로 변환해서 반환
    }

    @Override
    public Page<SchedulerFindResponseDto> findAllSchedules(SchedulerSearchCond searchCond, Pageable pageable) {

        LocalDate condDate = searchCond.getCondDate();  // 날짜 검색 조건
        String condName = searchCond.getCondName();     // 이름 검색 조건

        SqlParameterSource param = new BeanPropertySqlParameterSource(searchCond);

        // 동적 쿼리 시작
        String query = "select s.id, w.id, s.content, w.name, w.update_date" +
                " from schedule as s" +
                " join writer as w" +
                " on s.id = w.id";

        String allCountQuery = "select count(*) from schedule as s join writer as w on s.id = w.id";

        // 동적 쿼리 작성하기
        // 날짜가 null이 아니거나 이름이 null, 길이 0, 공백 문자만으로 구성되어있지 않으면 -> 즉 동적 쿼리 조건이 있으면 where 붙이기
        if (condDate != null || StringUtils.hasText(condName)) {
            query += " where";
            allCountQuery += " where";
        }

        boolean andFlag = false;    // and 조건 붙이기 위한 플래그
        if (condDate != null) {
            // DB의 update_date 컬럼의 타입이 시, 분, 초가 있으므로 날짜 조건만 맞추기 위해 like 문법 사용
            query += " update_date like concat(:condDate, '%')";
            allCountQuery += " update_date like concat(:condDate, '%')";
            andFlag = true;         // and 플래그 true 설정
        }

        if (StringUtils.hasText(condName)) {
            // 날짜 조건이 null이 아니라서 쿼리가 추가 되면 쿼리에 and 추가
            if (andFlag) {
                query += " and";
                allCountQuery += " and";

            }
            query += " name = :condName";   // 작성자 이름 같은 일정 조회
            allCountQuery += " name = :condName";

        }

        int offset = pageable.getPageNumber() * pageable.getPageSize();
        int limit = pageable.getPageSize();

        query += " order by update_date desc limit " + limit + " offset " + offset;

        List<SchedulerFindResponseDto> findAllSchedules = jdbcTemplate.query(query, param, scheduleRowMapper());

        Integer totalCount = jdbcTemplate.queryForObject(allCountQuery, param, Integer.class);

        // 쿼리 결과가 없으면 0으로 반환
        if (totalCount == null) {
            totalCount = 0;
        }

        return new PageImpl<>(findAllSchedules, pageable, totalCount);
    }

    @Override
    public Optional<SchedulerFindResponseDto> findScheduleById(Long id) {
        String query = "select s.id, w.id, s.content, w.name, w.update_date" +
                " from schedule as s" +
                " join writer as w" +
                " on s.id = w.id" +
                " where s.id = :id";

        Map<String, Long> param = Map.of("id", id);
        List<SchedulerFindResponseDto> findScheduleDto = jdbcTemplate.query(query, param, scheduleRowMapper());

        // Optional 반환
        return findScheduleDto.stream().findAny();
    }

    @Override
    public Optional<String> findPasswordById(Long id) {
        String query = "select password from Schedule where id = :id";
        Map<String, Long> param = Map.of("id", id);

        // 단건을 조회하는 queryForObject는 못찾으면 EmptyResultDataAccessException이 발생한다고 함
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(query, param, String.class));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    /**
     * 일정만 수정, 작성자 테이블의 수정일은 최신화
     *
     * @param id 수정할 일정의 ID
     * @param content 수정할 내용
     * @return 수정된 행의 개수
     */
    @Override
    public int updateScheduleContent(Long id, String content) {
        String query = "update schedule as s" +
                " join writer as w" +
                " on s.id = w.id" +
                " set s.content = :content, w.update_date = now()" +
                " where s.id = :id";

        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("id", id)
                .addValue("content", content);

        return jdbcTemplate.update(query, param);
    }

    /**
     * 작성자만 수정, 작성자 테이블의 수정일은 최신화
     *
     * @param id 수정할 일정의 ID
     * @param name 수정할 작성자 이름
     * @return 수정된 행의 개수
     */
    @Override
    public int updateWriterName(Long id, String name) {
        String query = "update writer set name = :name, update_date = now() where id = :id";

        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("id", id)
                .addValue("name", name);

        return jdbcTemplate.update(query, param);
    }

    /**
     * 작성자, 일정 모두 수정, 수정일 최신화
     *
     * @param id 수정할 일정의 ID
     * @param content 수정할 일정
     * @param name 수정할 작성자 이름
     * @return 수정된 행의 개수
     */
    @Override
    public int updateScheduleContentWithWriterName(Long id, String content, String name) {
        String query = "update schedule as s" +
                " join writer as w" +
                " on s.id = w.id" +
                " set s.content = :content, w.name = :name, w.update_date = now()" +
                " where s.id = :id";

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
