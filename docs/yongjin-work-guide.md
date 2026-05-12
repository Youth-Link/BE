# 용진 담당 구현 가이드

## 담당 범위

용진 담당 영역은 회원 프로필, RAG 채팅 API, 대화 이력, 시스템 프롬프트, 공통 응답/예외 처리 사용 방식이다.

## 이번 구현에서 바뀐 점

1. `MemberController`와 `ChatController`에서 임시값 `memberId = 1L`을 제거했다.
2. `CurrentMemberProvider`를 통해 JWT 인증 정보의 email로 현재 로그인한 `Member`를 조회한다.
3. 채팅 이력 조회/삭제 API를 추가했다.
4. 코드 안에 있던 시스템 프롬프트를 `src/main/resources/prompts/system-prompt.st`로 분리했다.
5. 테스트 실패 원인이던 중복 `SecurityConfig` 중 하나를 제거했다.
6. `GET /api/members/{memberId}`는 본인 ID만 조회할 수 있도록 제한했다.
7. 정책 검색 결과가 없을 때는 Gemini를 호출하지 않고 안내 응답을 바로 반환한다.

## API 흐름

### 내 프로필 조회

`GET /api/members/me`

1. JWT 필터가 `Authorization: Bearer {accessToken}`을 읽는다.
2. 토큰 subject에 들어있는 email이 `SecurityContext`에 저장된다.
3. `CurrentMemberProvider`가 email로 `Member`를 조회한다.
4. `MemberQueryService`가 회원 상세 DTO를 반환한다.

### 내 프로필 수정

`PUT /api/members/me`

요청 예시:

```json
{
  "name": "홍길동",
  "age": 27,
  "region": "서울",
  "education": "대학교 졸업",
  "employmentStatus": "미취업",
  "incomeLevel": "중위소득 100% 이하"
}
```

이 값들은 이후 RAG 검색의 metadata filtering 조건으로 사용할 수 있다.

### 정책 상담 채팅

`POST /api/v1/chat`

요청 예시:

```json
{
  "sessionId": "session-001",
  "message": "27살 서울 청년이 받을 수 있는 월세 지원 알려줘"
}
```

응답 예시:

```json
{
  "isSuccess": true,
  "code": "COMMON200",
  "message": "성공입니다.",
  "result": {
    "reply": "답변 내용",
    "sessionId": "session-001",
    "sources": []
  }
}
```

정책 검색 결과가 없으면 AI가 추측하지 않도록 Gemini 호출을 생략하고 고정 안내 문구를 반환한다.

### 대화 이력 조회

`GET /api/v1/chat/sessions/{sessionId}/messages`

세션별 저장된 사용자/AI 메시지를 시간순으로 반환한다.

### 대화 이력 삭제

`DELETE /api/v1/chat/sessions/{sessionId}`

현재 로그인 사용자의 해당 세션 메시지만 삭제한다.

## 핵심 코드 이해

`CurrentMemberProvider`는 컨트롤러에서 현재 로그인 사용자를 가져오기 위한 작은 유틸이다. 컨트롤러가 JWT나 SecurityContext의 세부 구현을 직접 알 필요가 없게 해준다.

`ChatServiceImpl`은 실제 RAG 흐름을 담당한다.

1. 사용자 질문으로 Chroma Vector DB에서 관련 정책을 검색한다.
2. 검색된 정책을 context 문자열로 만든다.
3. `system-prompt.st` 템플릿에 context를 넣는다.
4. 최근 대화 이력을 함께 Gemini에 전달한다.
5. 답변과 출처를 저장하고 반환한다.

## 용진 파트에서 다음에 보면 좋은 것

- `MemberController`: 현재 로그인 사용자 기반 프로필 API
- `ChatController`: 채팅 요청, 이력 조회, 이력 삭제 API
- `ChatServiceImpl`: RAG 채팅의 실제 흐름
- `ChatMessageRepository`: 세션별 대화 이력 조회
- `system-prompt.st`: AI 답변 규칙
