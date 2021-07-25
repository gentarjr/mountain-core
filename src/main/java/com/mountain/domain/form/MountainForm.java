package com.mountain.domain.form;

import lombok.Data;

import java.io.Serializable;

@Data
public class MountainForm implements Serializable {

    private static final long serialVersionUID = 1L;

    private String mountainName;
    private String basecampName;
    private String description;
    private Integer height;
    private String regulation;
    private String fullAddress;
    private Double price;
    private String updatedBy;
}
