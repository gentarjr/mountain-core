package com.mountain.domain.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class ReplyStatusResponse implements Serializable {

    private String id;
    private String username;
    private String role;
    private String reply;
    private String createdDate;
}
