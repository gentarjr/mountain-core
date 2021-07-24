package com.mountain.domain.form;

import lombok.Data;

import java.util.List;


@Data
public class UserForm {

    //table user
    private String id;
    private String idCard;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String mountainName;
    private String email;
    private String pin;
    private String confirmationPin;
    private String fullAddress;
    private Integer totalClimber;
    private Double paymentClimber;
    private String status;
    private String reason;
    private String role;
    private String token;
    private String bankName;
    private String accountName;
    private String accountNumber;
    private List<RequestMountainForm> requestMountain;

}
