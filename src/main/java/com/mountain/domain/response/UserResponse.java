package com.mountain.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mountain.entity.role.Role.ERole;
import com.mountain.library.helper.DateUtils;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse implements Serializable {

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
    private String updatedDate;


    public UserResponse(String id, String nik, String username, String firstName, String lastName,
                        String phoneNumber, String address, String email, ERole role, String token, LocalDateTime createdDate, LocalDateTime updatedDate) {

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
        this.createdDate = DateUtils.formatDateTimeOrEmptyString(createdDate);
        this.updatedDate = DateUtils.formatDateTimeOrEmptyString(updatedDate, "dd/MMM/yyyy kk:mm");
    }
}
