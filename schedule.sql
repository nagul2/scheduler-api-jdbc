-- 필수 테이블 생성문
create table schedule (
    id bigint auto_increment primary key comment '일정 식별자',
    content varchar(255) comment '할일',
    name varchar(10) comment '일정 작성자',
    password varchar(40) comment '비밀번호-UUID',
    create_date datetime comment '생성일',
    update_date datetime comment '수정일'
)