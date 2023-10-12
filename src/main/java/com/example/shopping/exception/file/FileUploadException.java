package com.example.shopping.exception.file;

// 파일 업로드 예외처리
public class FileUploadException extends RuntimeException{
    public FileUploadException(String message) {
        super(message);
    }
}
