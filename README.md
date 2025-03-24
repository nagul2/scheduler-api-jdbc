
# 도전 구현 3단계
### 필수 구현 코드를 유지하기 위해 challenge 패키지를 별도로 추가로 생성하여 작성
___

## 📍개발 내용
### 테이블 분리 적용
- Entity를 Writer와 Schedule로 분리하고 테이블도 각각 생성
- Schedule 에는 WriterId를 필드로 가지고 있으며 해당 값은 외래키임

### API 수정 - 테이블 분리로 인한 삭제를 제외한 모든 API 수정
- 일정 생성 API
  - 테이블을 분리했으므로 Service 계층에서 Writer를 먼저 생성하여 저장한 후 반환된 Key값으로 일정을 생성하여 DB에 일정을 저장
  - repository에서 Writer를 저장하는 saveWriter()와 Schedule saveSchedule()로 분리해서 각각 저장
  - 서비스 계층에서 하나의 트랜잭션으로 묶었으므로 repository의 두개의 메서드가 모두 실패하거나 모두 성공하도록 작성


- 일정 단건 조회, 전체 조회 API
  - schedule 테이블과 writer 테이블을 조인하는 구문을 추가
  - 로직은 동일


- 일정 수정 API
  - 이름만 수정, 일정만 수정, 둘다 수정하는 메서드로 각각 분리하여 service 계층에서 동적으로 호출하도록 수정
    - 일정만 수정: updateScheduleContent()
    - 이름만 수정: updateWriterName()
    - 둘다 수정: updateScheduleContentWithWriterName()
  - 일정만 수정하거나 이름만 수정할 경우 각각의 테이블에서 단순한 update문으로 수정
  - 둘다 수정해야하는 경우 테이블이 다르기 때문에 각각 따로 수정해야하지만 MySQL에서 지원하는 update join 문을 활용하여 한번의 쿼리로 수정하도록 함
