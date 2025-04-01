package spring.basic.scheduler.challenge.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import spring.basic.scheduler.challenge.model.entity.Writer;

import javax.sql.DataSource;

@Repository
public class WriterRepositoryImpl implements WriterRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert writerJdbcInsert;

    public WriterRepositoryImpl(DataSource dataSource) {
        // 바인딩 순서로 쿼리하면 버그가 생길 수 있으므로 파라미터 이름으로 쿼리를 할 수 있는 JdbcTemplate
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

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

}
