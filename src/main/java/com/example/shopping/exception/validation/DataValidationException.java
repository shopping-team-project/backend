package com.example.shopping.exception.validation;

// 검증 오류 처리
// 데이터 유효성 검사 실패 시 발생하는 예외
public class DataValidationException extends RuntimeException{
    public DataValidationException(String message) {
        super(message);
    }
}
