package com.mountain.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mountain.entity.role.Role.ERole;
import com.mountain.library.helper.DateUtils;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class UserResponse {

    private static final long serialVersionUID = 1L;

    private String id;
    private String nik;
    private String username;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;
    private String email;
    private ERole role;
    private String token;
    private String createdDate;
    private Timestamp updatedDate;


    public UserResponse(String id, String nik, String username, String firstName, String lastName,
                        String phoneNumber, String address, String email, ERole role, String token, Timestamp createdDate, Timestamp updatedDate) {

        this.id = id;
        this.nik = nik;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.email = email;
        this.role = role;
        this.token = token;
        this.createdDate = DateUtils.formatDateTimeOrEmptyString(createdDate.toLocalDateTime());
        this.updatedDate = updatedDate;
    }
}
