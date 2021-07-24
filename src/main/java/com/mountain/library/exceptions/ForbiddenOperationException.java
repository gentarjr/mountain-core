package com.mountain.library.exceptions;

import com.mountain.library.domain.ErrCode;

public class ForbiddenOperationException extends WinterfellException {

    public ForbiddenOperationException(ErrCode errCode, String message) {
        super(errCode, message);
    }
}
