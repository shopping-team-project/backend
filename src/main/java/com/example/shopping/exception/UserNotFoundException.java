package com.example.shopping.exception;

public class UserNotFoundException extends UserException{

    private static final long serialVersionUID = 1L;

    public UserNotFoundException(String message) {
        super(message);
    }
}