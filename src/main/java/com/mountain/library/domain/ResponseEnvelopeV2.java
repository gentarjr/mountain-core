package com.mountain.library.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.hateoas.RepresentationModel;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class ResponseEnvelopeV2<T> extends RepresentationModel<ResponseEnvelopeV2<T>> {
    private ResponseMeta meta = new ResponseMeta(ErrCode.SUCCESS.getCode() + "", ErrCode.SUCCESS.getMessage());
    private T data;

    public ResponseEnvelopeV2(String code, String message) {
        this.meta.setCode(code);
        this.meta.setMessage(message);
    }

    public ResponseEnvelopeV2(T data) {
        this.data = data;
    }
}
