package com.mountain.library.exceptions;

import com.mountain.library.domain.ErrCode;

public class EmptyFieldException extends WinterfellException {

    public EmptyFieldException(ErrCode errCode, String message) {
        super(errCode, message);
    }
}
