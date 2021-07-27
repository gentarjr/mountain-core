package com.mountain.domain.form;

import lombok.Data;

@Data
public class BasecampForm {

    private String basecampId;
    private String mountainId;
    private String basecampName;
    private String description;
    private String regulation;
    private String fullAddress;
    private Double price;
    private Integer totalClimber;

}
