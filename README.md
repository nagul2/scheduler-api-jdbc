
# 필수 구현 2단계
___

## 📍개발 내용
### 일정 수정 API 개발
- controller: updateSchedule()
    - 요청값으로 받은 수정할 일정의 id와 수정할 값들을 service 계층으로 전달
    - 메서드 호출 결과를 상태코드와 함게 ResponseEntity로 반환 


- service: updateSchedule()
    - 전달 받은 id로 repository 계층의 findPasswordById()를 호출하여 비밀번호를 찾고 검증
    - 검증이 실패하면 단순히 null을 반환하도록 예외 처리
    - 검증이 통과하면 업데이트를 하기 위해 repository 계층의 update 메서드를 호출
    - 반환된 메서드의 결과가 0이라면 일정을 찾을 수 없으므로 단순하게 null을 반환하도록 처리
    - 수정이 완료되면 수정된 일정의 id를 공통 응답 DTO를 생성하여 반환(일정 저장과 같은 응답 DTO)


- repository
  - findPasswordById()
      - 비밀번호 검증을 위해 DB에 저장된 패스워드를 가져와서 service 계층에서 해당 결과로 비밀번호 검증을 수행함
      - jdbcTemplate의 단건을 조회하는 queryForObject()를 사용할 경우 결과를 찾지 못하면 예외를 발생하기 때문에 try - catch로 예외 처리 진행


  - updateSchedule()
    - 비밀번호 검증이 통과되면 실제 DB에 저장된 일정에 매개변수로 넘어온 요청값을 수정 반영
    - 반환값으로 수정된 결과의 행수를 반환함

### 일정 삭제 API 개발
- controller: deleteSchedule()
    - 매개 변수로 전달된 id와 비밀번호가 담긴 DTO 객체를 service 계층으로 전달
    - 메서드 호출 결과를 별도로 응답할 필요가 없으므로 ResponseEntity로 응답할 때 상태코드만 반환함


- service: deleteSchedule()
    - 수정과 마찬가지로 findPasswordById()를 통해 비밀번호 검증을 하고 검증이 통과되면 repository 계층의 삭제 메서드를 호출함
    - 요구사항에 따라 다르겠지만 현재는 삭제 시 반환값을 주지 않아도 되므로 void로 작성됨


- repository: deleteSchedule()
    - 매개변수로 전달받은 일정을 삭제
    - jdbcTemplate에서는 삭제도 update()를 호출하여 처리하기 때문에 응답 결과를 수정 로직과 마찬가지로 삭제된 개수를 반환함

### ETC
- 필수 CRUD API 개발 후 주석 추가
