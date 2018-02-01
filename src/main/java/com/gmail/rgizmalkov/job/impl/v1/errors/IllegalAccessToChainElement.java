package com.gmail.rgizmalkov.job.impl.v1.errors;

public class IllegalAccessToChainElement extends RuntimeException {
    public IllegalAccessToChainElement() {
        super();
    }

    public IllegalAccessToChainElement(String message) {
        super(message);
    }

    public IllegalAccessToChainElement(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalAccessToChainElement(Throwable cause) {
        super(cause);
    }

    protected IllegalAccessToChainElement(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
