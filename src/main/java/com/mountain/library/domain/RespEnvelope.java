package com.mountain.library.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class RespEnvelope<T> {
    private RespMeta meta;
    private T data;

    public RespEnvelope() {
    }

    public RespMeta getMeta() {
        return this.meta;
    }

    public T getData() {
        return this.data;
    }

    public void setMeta(final RespMeta meta) {
        this.meta = meta;
    }

    public void setData(final T data) {
        this.data = data;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof RespEnvelope)) {
            return false;
        } else {
            RespEnvelope<?> other = (RespEnvelope)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$meta = this.getMeta();
                Object other$meta = other.getMeta();
                if (this$meta == null) {
                    if (other$meta != null) {
                        return false;
                    }
                } else if (!this$meta.equals(other$meta)) {
                    return false;
                }

                Object this$data = this.getData();
                Object other$data = other.getData();
                if (this$data == null) {
                    if (other$data != null) {
                        return false;
                    }
                } else if (!this$data.equals(other$data)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof RespEnvelope;
    }

    public String toString() {
        RespMeta var10000 = this.getMeta();
        return "RespEnvelope(meta=" + var10000 + ", data=" + this.getData() + ")";
    }
}