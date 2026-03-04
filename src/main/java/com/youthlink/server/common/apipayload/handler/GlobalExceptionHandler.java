package com.youthlink.server.common.apipayload.handler;

import com.youthlink.server.common.apipayload.ApiResponse;
import com.youthlink.server.common.apipayload.code.BaseErrorCode;
import com.youthlink.server.common.apipayload.code.GeneralErrorCode;
import com.youthlink.server.common.apipayload.exception.GeneralException;
import jakarta.validation.ConstraintViolationException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // 1. @Valid 유효성 검사 실패 시 (RequestBody)
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return handleExceptionInternal(e, GeneralErrorCode.VALID_FAIL, headers, status, request, errorMessage);
    }

    // 2. @RequestParam, @PathVariable 유효성 검사 실패 시
    @ExceptionHandler
    public ResponseEntity<Object> validation(ConstraintViolationException e, WebRequest request) {
        String errorMessage = e.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.joining(", "));

        return handleExceptionInternal(e, GeneralErrorCode.VALID_FAIL, HttpHeaders.EMPTY,
                GeneralErrorCode.BAD_REQUEST.getStatus(), request, errorMessage);
    }

    // 3. 프로젝트 커스텀 예외(GeneralException) 처리: 우리가 직접 정의한 비즈니스 로직 에러
    @ExceptionHandler(value = GeneralException.class)
    public ResponseEntity<Object> onGeneralException(GeneralException exception, WebRequest request) {
        return handleExceptionInternal(exception, exception.getCode(), HttpHeaders.EMPTY,
                exception.getCode().getStatus(), request, null);
    }

    // 4. 정의되지 않은 모든 일반 예외 처리
    @ExceptionHandler
    public ResponseEntity<Object> exception(Exception e, WebRequest request) {
        log.error("Unhandled Exception 발생: ", e);
        return handleExceptionInternal(e, GeneralErrorCode.INTERNAL_SERVER_ERROR, HttpHeaders.EMPTY,
                GeneralErrorCode.INTERNAL_SERVER_ERROR.getStatus(), request, e.getMessage());
    }

    // 응답 생성 공통 로직: 모든 예외 응답을 최종적으로 우리 프로젝트의 ApiResponse 포맷으로 통일함
    // 부모 클래스가 표준 예외를 처리할 때 마지막에 이 메서드를 호출하므로 여기만 구현해두면 모든 예외가 통일됨
    private ResponseEntity<Object> handleExceptionInternal(Exception e, BaseErrorCode code, HttpHeaders headers,
            HttpStatusCode status, WebRequest request, String errorPoint) {
        ApiResponse<Object> body = ApiResponse.onFailure(code.getCode(), code.getMessage(), errorPoint);
        return super.handleExceptionInternal(e, body, headers, status, request);
    }
}
