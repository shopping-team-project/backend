package com.example.shopping.exception.file;

// 파일 다운로드 예외처리
public class FileDownloadException extends RuntimeException{
    public FileDownloadException(String message) {
        super(message);
    }
}
