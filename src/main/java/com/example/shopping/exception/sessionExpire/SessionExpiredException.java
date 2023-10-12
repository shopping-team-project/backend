package com.example.shopping.exception.sessionExpire;

// 인증 예외처리
public class SessionExpiredException extends RuntimeException{
    public SessionExpiredException(String message) {
        super(message);
    }
}
