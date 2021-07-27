package com.mountain.domain.form;

import lombok.Data;

import java.util.List;

@Data
public class UserForm {

    //table user
    private String id;
    private String nik;
    private String phoneNumber;
    private String username;
    private String firstName;
    private String lastName;
    private String address;
    private String email;
    private String bankName;
    private String accountName;
    private Integer accountNumber;
    private String pin;
    private String confirmationPin;
    private String role;

    private String status;
    private String replyStatus;
    private String statusId;
    private String mountainId;
}
