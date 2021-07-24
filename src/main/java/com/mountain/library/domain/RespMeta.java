package com.mountain.library.domain;

public class RespMeta {
    private String code;
    private String message;

    public RespMeta() {
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof RespMeta)) {
            return false;
        } else {
            RespMeta other = (RespMeta)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$code = this.getCode();
                Object other$code = other.getCode();
                if (this$code == null) {
                    if (other$code != null) {
                        return false;
                    }
                } else if (!this$code.equals(other$code)) {
                    return false;
                }

                Object this$message = this.getMessage();
                Object other$message = other.getMessage();
                if (this$message == null) {
                    if (other$message != null) {
                        return false;
                    }
                } else if (!this$message.equals(other$message)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof RespMeta;
    }

    public String toString() {
        String var10000 = this.getCode();
        return "RespMeta(code=" + var10000 + ", message=" + this.getMessage() + ")";
    }
}
