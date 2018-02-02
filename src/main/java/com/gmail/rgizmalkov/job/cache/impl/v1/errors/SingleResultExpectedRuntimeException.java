package com.gmail.rgizmalkov.job.cache.impl.v1.errors;

public class SingleResultExpectedRuntimeException extends RuntimeException {
    public SingleResultExpectedRuntimeException() {
    }

    public SingleResultExpectedRuntimeException(String message) {
        super(message);
    }

    public SingleResultExpectedRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public SingleResultExpectedRuntimeException(Throwable cause) {
        super(cause);
    }

    public SingleResultExpectedRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
