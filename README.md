# 필수 구현 1단계
___

## 📍개발 내용
### 단일 테이블로 일정 관리 API 개발 시작
- 3 Layered Architecture 적용
- Schedule Entity 개발

### 일정 생성 API 개발
- controller: addSchedule()
  - 클라이언트에서 요청받은 데이터를 service 계층으로 전달
  - 메서드 호출 결과를 ResponseEntity로 상태코드와 함께 반환


- service: saveSchedule()
  - 매개변수로 전달받은 요청 데이터를 통해 Schedule 객체를 생성
  - 생성한 객체를 DB에 저장하기 위해 repository 계층으로 전달
  - 응답을 위한 DTO를 메서드 호출 결과인 생성한 일정의 ID를 인자로하여 생성 후 반환


- repository: saveSchedule()
  - NamedParameterJdbcTemplate, SimpleJdbcInsert를 활용하여 넘겨받은 Schedule 객체를 DB에 저장
  - 저장 후 DB에 자동 생성된 Key값을 Long 타입으로 반환

### 일정 전체 조회 API 개발
- controller: findSchedules()
  - 매개변수로 전달받은 검색조건(날짜, 작성자 이름)을 service 계층으로 전달
  - 메서드 호출결과를 List로 반환


- service: findAllSchedules()
  - 컨트롤러와 동일하게 매개변수로 전달 받은 검색 조건을 repository 계층으로 전달하고 List로 반환


- repository: findAllSchedules()
  - NamedParameterJdbcTemplate을 활용하여 검색 조건을 분기에 따라 이어붙여서 동적 쿼리를 적용
  - 검색 조건이 없다면 전체를 조건이 없이 조회
  - 검색 조건이 모두 있다면 모두 적용
  - 검색 조건이 둘 중에 하나만 있다면 하나만 적용함
  - 메서드의 반환값을 별도의 List<DTO>로 지정하여 필요한 정보만 반환하도록 RowMapper를 해당 DTO로 지정

### 일정 단건 조회 API 개발
- controller: findSchedule()
  - 요청값으로 넘어온 id를 service 계층으로 전달하여 id에 맞는 일정을 조회
  - 반환된 결과를 ResponseEntity로 응답코드와 함께 반환


- service: findScheduleById()
  - repository 계층의 메서드로 id값을 넘겨서 일정을 조회하고 Optional의 결과를 예외처리 후 Optional의 내용을 반환
  - 성공 케이스만 구현하는 단계이므로 단순히 Optional이 비어있으면 null을 반환하도록 함


- repository: findScheduleById()
  - 반환값을 Optional<DTO>로 적용하여 null에 안전하게 반환하도록 작성
  - service 계층에서 넘겨받은 id 값을 가지고 DB에서 일정을 조회하여 반환하며 전체 조회와 동일한 DTO를 사용함