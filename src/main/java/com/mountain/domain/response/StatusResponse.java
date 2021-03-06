package com.mountain.domain.response;

import lombok.Data;

import java.util.List;

@Data
public class StatusResponse {

    private String id;
    private String status;
    private String username;
    private String role;
    private String photo;
    private String createdDate;

    private List<ReplyStatusResponse> replyStatus;
}
