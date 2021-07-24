package com.mountain.library.exceptions;


import com.mountain.library.domain.ErrCode;

public class WinterfellException extends RuntimeException {

    private ErrCode errCode;

    public WinterfellException(String message) {
        super(message);
    }

    public WinterfellException(ErrCode errCode, String message) {
        super(message);
        this.errCode = errCode;
    }

    public WinterfellException(ErrCode errCode, String message, Throwable cause) {
        super(message, cause);
        this.errCode = errCode;
    }

    public WinterfellException(String message, Throwable cause) {
        super(message, cause);
    }

    public ErrCode getErrCode() {
        return errCode;
    }

    public void setErrCode(ErrCode errCode) {
        this.errCode = errCode;
    }

    @Override
    public String toString() {
        return "MountainException{" + "errCode=" + errCode + '}';
    }
}
