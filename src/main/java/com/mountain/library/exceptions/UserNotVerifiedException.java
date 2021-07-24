package com.mountain.library.exceptions;

import com.mountain.library.domain.ErrCode;

public class UserNotVerifiedException extends WinterfellException{

    public UserNotVerifiedException(ErrCode errCode, String message) {
        super(errCode, message);
    }
}
