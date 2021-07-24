package com.mountain.library.exceptions;

public class PreexistingEntityException extends WinterfellException {

    public PreexistingEntityException(String message, Throwable cause) {
        super(message, cause);
    }
    public PreexistingEntityException(String message) {
        super(message);
    }
}
