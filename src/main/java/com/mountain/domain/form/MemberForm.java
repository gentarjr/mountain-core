package com.mountain.domain.form;

import lombok.Data;

@Data
public class MemberForm {

    private String name;
    private String idCard;
    private String phoneNumber;
    private String provinsi;
    private String kota;
    private String kecamatan;
    private String rtRw;
}
