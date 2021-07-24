package com.mountain.library.exceptions;

import com.mountain.library.domain.ErrCode;
import org.springframework.security.core.AuthenticationException;

public class JwtTokenException extends AuthenticationException {
    private ErrCode errCode;

    public JwtTokenException(ErrCode errCode, String msg) {
        super(msg);
        this.errCode = errCode;
    }

    public JwtTokenException(ErrCode errCode, String msg, Throwable t) {
        super(msg, t);
        this.errCode = errCode;
    }

    public void setErrCode(ErrCode errCode) {
        this.errCode = errCode;
    }

    public ErrCode getErrCode() {
        return errCode;
    }
}
