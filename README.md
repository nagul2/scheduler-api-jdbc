# 도전 구현 5단계
### 필수 구현 코드를 유지하기 위해 challenge 패키지를 별도로 추가로 생성하여 작성
___

## 📍개발 내용
### API 예외 처리 적용
- 예외 처리를 위한 애플리케이션 전용 Runtime 예외 생성
  - NoSuchScheduleException: 일정을 찾지 못했을 때
  - PasswordValidationException: 비밀번호 검증이 실패했을 때


- ErrorCode를 상수로 관리하기 위해 Enum 객체 생성
- 예외 처리 시 메시지를 응답하기 위핸 GlobalExceptionHandler 생성 
- 단순히 null 로 반환하던 service 계층의 예외 처리 로직을 생성한 예외를 반환하도록 작성


### Refactoring
- Service 계층의 복잡한 코드들을 각각 기능에 맞게 메서드화