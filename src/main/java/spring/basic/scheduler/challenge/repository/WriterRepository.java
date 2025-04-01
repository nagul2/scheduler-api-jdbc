package spring.basic.scheduler.challenge.repository;

import spring.basic.scheduler.challenge.model.entity.Writer;

public interface WriterRepository {

    Long saveWriter(Writer writer);
    int updateWriterName(Long id, String name);

}
