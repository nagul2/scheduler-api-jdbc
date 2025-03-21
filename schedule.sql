-- 필수 테이블 생성문
create table schedule
(
    id          bigint auto_increment primary key comment '일정 식별자',
    content     varchar(255) comment '할일',
    name        varchar(10) comment '일정 작성자',
    password    varchar(40) comment '비밀번호',
    create_date datetime comment '생성일',
    update_date datetime comment '수정일'
)

-- 도전 테이블 생성문
create table writer
(
    id          bigint auto_increment primary key comment '작성자 식별자',
    name        varchar(10) comment '작성자',
    email       varchar(50) comment 'email',
    create_date datetime comment '생성일',
    update_date datetime comment '수정일'
)

create table schedule
(
    id        bigint auto_increment primary key comment '일정 식별자',
    writer_id bigint not null comment '작성자 외래키',
    content   varchar(255) comment '할일',
    password  varchar(40) comment '비밀번호',
    foreign key (writer_id) references writer (id)
)

