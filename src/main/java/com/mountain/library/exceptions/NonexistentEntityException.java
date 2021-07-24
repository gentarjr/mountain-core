package com.mountain.library.exceptions;

import com.mountain.library.domain.ErrCode;

public class NonexistentEntityException extends WinterfellException {

    public NonexistentEntityException(String message) {
        super(message);
    }

    public NonexistentEntityException(ErrCode errCode, String message) {
        super(errCode, message);
    }

    public NonexistentEntityException(String message, Throwable cause) {
        super(message, cause);
    }
}
