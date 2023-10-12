package com.example.shopping.exception.service;

// 서비스 로직 예외 처리
// 서비스 또는 비즈니스 로직의 일부에서 발생한 문제를 나타냅니다.
// 예를 들어, 특정 제품의 재고가 부족한 경우에 이 예외를 발생시킬 수 있습니다.
public class OutOfStockException extends RuntimeException{
    public OutOfStockException(String message) {
        super(message);
    }
}
