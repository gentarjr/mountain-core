package com.mountain.entity.user;

import com.mountain.entity.role.Role;
import com.mountain.entity.role.Role.ERole;
import com.mountain.library.helper.CodecUtils;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity
@Table(name = "users")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Column(name = "mountain_id")
    private String mountainId;

    @Column(name = "nik")
    private String nik;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "username")
    private String username;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "address")
    private String address;

    @Column(name = "email")
    private String email;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "account_name")
    private String accountName;

    @Column(name = "account_number")
    private Integer accountNumber;

    @Column(name = "pin")
    private String pin;

    @Column(name = "confirmation_pin")
    private String confirmationPin;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private ERole role;

    @Column(name = "created_date")
    private Timestamp createdDate;

    @Column(name = "updated_date")
    private Timestamp updatedDate;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_role", joinColumns = @JoinColumn(name = "id"))
    private Set<Role> roles;

    public User(){
        this.id = CodecUtils.generateUUID();
    }

    public User(String nik, String phoneNumber, String username, String firstName,
                String lastName, String address, String email, String pin, String confirmationPin, ERole role, Timestamp createdDate) {
        this();
        this.nik = nik;
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.email = email;
        this.pin = pin;
        this.confirmationPin = confirmationPin;
        this.role = role;
        this.createdDate = createdDate;
    }
}
