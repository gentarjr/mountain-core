package com.mountain.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class MemberResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String idCard;
    private String name;
    private String phoneNumber;
    private String provinsi;
    private String kota;
    private String kecamatan;
    private String rtRw;
    private String createdDate;
    private String updatedDate;
}
