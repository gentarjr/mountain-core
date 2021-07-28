package com.mountain.domain.response;

import lombok.Data;

@Data
public class StatusResponse {

    private String id;
    private String status;
    private String username;
    private String role;
    private String photo;
    private String createdDate;
}
