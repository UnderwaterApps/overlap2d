package com.runner.exception;

public class LibgdxInitException extends RuntimeException {
    public LibgdxInitException(Throwable cause) {
        super("Libgdx application init failed", cause);
    }
}
