package com.runner.exception;

public class TimeoutException extends RuntimeException {
    public TimeoutException(String description) {
        super(description);
    }
}
