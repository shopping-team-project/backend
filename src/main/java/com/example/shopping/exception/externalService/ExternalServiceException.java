package com.example.shopping.exception.externalService;

// 외부 서비스 연동 예외 처리
// 외부 서비스와의 통신 또는 연동 중에 문제가 발생했을 때 사용됩니다.
// 외부 서비스로의 HTTP 요청 또는 API 호출 등과 관련된 예외 상황을 처리합니다.
public class ExternalServiceException extends RuntimeException{
    public ExternalServiceException(String message) {
        super(message);
    }
}
