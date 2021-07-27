package com.mountain.entity.detail;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
public class Member implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Column(name = "request_id")
    private String requestId;

    @Column(name = "nik")
    private String nik;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "name")
    private String name;

    @Column(name = "province")
    private String province;

    @Column(name = "head")
    private Boolean head;

    @Column(name = "city")
    private String city;

    @Column(name = "sub_district")
    private String subDistrict;

    @Column(name = "village")
    private String village;

    @Column(name = "full_address")
    private String fullAddress;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = " updated_date")
    private LocalDateTime updatedDate;

}
