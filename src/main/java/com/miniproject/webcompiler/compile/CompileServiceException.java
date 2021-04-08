package com.miniproject.webcompiler.compile;

public class CompileServiceException extends RuntimeException {

    public CompileServiceException(String message) {
        super(message);
    }

    public CompileServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
