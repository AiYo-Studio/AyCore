package com.mc9y.blank038api.exception;

public class ErrorJavaPluginException extends Exception{

    public ErrorJavaPluginException(Throwable cause) {
        super(cause);
    }

    public ErrorJavaPluginException() {
    }

    public ErrorJavaPluginException(String message, Throwable cause) {
        super(message, cause);
    }

    public ErrorJavaPluginException(String message) {
        super(message);
    }
}
