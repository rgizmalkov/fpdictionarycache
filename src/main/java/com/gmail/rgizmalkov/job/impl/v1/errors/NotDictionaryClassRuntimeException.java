package com.gmail.rgizmalkov.job.impl.v1.errors;

public class NotDictionaryClassRuntimeException extends RuntimeException {
    public NotDictionaryClassRuntimeException() {
        super();
    }

    public NotDictionaryClassRuntimeException(String message) {
        super(message);
    }

    public NotDictionaryClassRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotDictionaryClassRuntimeException(Throwable cause) {
        super(cause);
    }

    protected NotDictionaryClassRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
