package com.mountain.library.exceptions;

import com.mountain.library.domain.ErrCode;

public class InvalidFieldException extends WinterfellException {

    public InvalidFieldException(ErrCode errCode, String message) {
        super(errCode, message);
    }
}
