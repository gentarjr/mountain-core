package com.mountain.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mountain.entity.role.Role;
import com.mountain.library.helper.DateUtils;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class UserResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String idCard;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String mountainName;
    private String email;
    private String status;
    private String reason;
    private String address;
    private Integer totalClimber;
    private Double paymentClimber;
    private List<RequestMountainResponse> requestMountain;
    private Role.ERole role;
    private String roleName;
    private String bankName;
    private String accountName;
    private String accountNumber;
    private String accessToken;

    private String createdDate;
    private String updatedDate;

    public UserResponse(){

    }

    public UserResponse(String token, String id, String idCard, String firstName,
                        String lastName, String phoneNumber, String email, String address, Role.ERole role,
                        String bankName, String accountName, String accountNumber, List<RequestMountainResponse> requestMountain,
                         LocalDateTime createdDate, LocalDateTime updatedDate) {
        this.accessToken = token;
        this.id = id;
        this.idCard = idCard;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.role = role;
        this.bankName = bankName;
        this.accountName = accountName;
        this.accountNumber = accountNumber;
        this.requestMountain = requestMountain;
        this.createdDate = DateUtils.formatDateTimeOrEmptyString(createdDate);
        this.updatedDate = DateUtils.formatDateTimeOrEmptyString(updatedDate, "dd/MMM/yyyy kk:mm");
    }

    public UserResponse(String token, String id, String idCard, String firstName, String lastName, String mountainName, String phoneNumber,
                        String email, String address, String roleName, LocalDateTime createdDate, LocalDateTime updatedDate) {
        this.accessToken = token;
        this.id = id;
        this.idCard = idCard;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mountainName = mountainName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.roleName = roleName;
        this.createdDate = DateUtils.formatDateTimeOrEmptyString(createdDate);
        this.updatedDate = DateUtils.formatDateTimeOrEmptyString(updatedDate, "dd/MMM/yyyy kk:mm");
    }
}
