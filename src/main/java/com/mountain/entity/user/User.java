package com.mountain.entity.user;

import com.mountain.entity.detail.RequestMountain;
import com.mountain.entity.role.Role;
import com.mountain.entity.role.RoleMountain;
import com.mountain.library.helper.CodecUtils;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "users")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    private String idCard;

    private String phoneNumber;

    private String mountainName;

    private String firstName;

    private String lastName;

    private String address;

    private String email;

    private String bankName;

    private String accountName;

    private String accountNumber;

    private Double paymentClimber;

    private String pin;

    private String confirmationPin;

    @Enumerated(EnumType.STRING)
    private Role.ERole role;

    private String roleName;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    @ToString.Exclude
    @OneToMany(mappedBy = "user")
    private List<RequestMountain> requestMountain;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_role", joinColumns = @JoinColumn(name = "id"))
    private Set<Role> roles;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "mountain_role", joinColumns = @JoinColumn(name = "id"))
    private Set<RoleMountain> roleMountain;

    public User() {

    }

    public User(String idCard, String phoneNumber, String firstName, String lastName,
                String fullAddress, String email, String bankName, String accountName,
                String accountNumber, String pin, String confirmationPin,
                Role.ERole roleUser) {
        this.id = CodecUtils.generateUUID();
        this.idCard = idCard;
        this.phoneNumber = phoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = fullAddress;
        this.email = email;
        this.bankName = bankName;
        this.accountName = accountName;
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.confirmationPin = confirmationPin;
        this.role = roleUser;
        this.createdDate = LocalDateTime.now();
    }

    public User(String idCard, String phoneNumber, String firstName, String lastName, String mountainName,
                String fullAddress, Double paymentClimber, String role, String pin, String confirmationPin) {
        this.id = CodecUtils.generateUUID();
        this.idCard = idCard;
        this.phoneNumber = phoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mountainName = mountainName;
        this.address = fullAddress;
        this.paymentClimber = paymentClimber;
        this.roleName  = role;
        this.pin = pin;
        this.confirmationPin = confirmationPin;
    }
}
