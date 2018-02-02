package com.gmail.rgizmalkov.job.cache.impl.v1.errors;

public class AnyResultExpectedRuntimeException extends RuntimeException {
    public AnyResultExpectedRuntimeException() {
    }

    public AnyResultExpectedRuntimeException(String message) {
        super(message);
    }

    public AnyResultExpectedRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public AnyResultExpectedRuntimeException(Throwable cause) {
        super(cause);
    }

    public AnyResultExpectedRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
