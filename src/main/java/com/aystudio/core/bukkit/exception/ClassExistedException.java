package com.aystudio.core.bukkit.exception;

public class ClassExistedException extends Exception{

    public ClassExistedException(Throwable cause) {
        super(cause);
    }

    public ClassExistedException() {
    }

    public ClassExistedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClassExistedException(String message) {
        super(message);
    }
}
