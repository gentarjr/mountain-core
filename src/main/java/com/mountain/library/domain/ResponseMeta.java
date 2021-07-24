package com.mountain.library.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseMeta {

    private String code;
    private String message;
}
