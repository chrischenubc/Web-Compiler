package com.miniproject.compiler_service.compile;

public class UnknownLanguageException extends RuntimeException{
    public UnknownLanguageException(String message) {
        super(message);
    }
}
