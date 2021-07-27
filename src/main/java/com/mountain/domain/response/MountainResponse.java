package com.mountain.domain.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class MountainResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String photo;
    private String mountainName;
    private String description;
    private Integer height;
    private String fullAddress;


}
