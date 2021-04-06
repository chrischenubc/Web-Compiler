package com.miniproject.webcompiler.compile;

public class CompileFailureException extends RuntimeException {

    public CompileFailureException(String message) {
        super(message);
    }

    public CompileFailureException(String message, Throwable cause) {
        super(message, cause);
    }

}
