package com.mountain.library.exceptions;

import com.mountain.library.domain.ErrCode;

public class PreexistingUserException extends WinterfellException{
    public PreexistingUserException(ErrCode errCode, String message) {
        super(errCode, message);
    }
}
