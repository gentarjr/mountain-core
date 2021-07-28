package com.mountain.domain.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class BasecampResponse implements Serializable {

    private String id;
    private String basecampName;
    private String description;
    private String photo;
    private String regulation;
    private String fullAddress;
    private Double price;
    private Integer totalClimber;
    private String requestClimber;
    private String createdDate;
    private String createdBy;
    private String updatedDate;
    private String updatedBy;
}
