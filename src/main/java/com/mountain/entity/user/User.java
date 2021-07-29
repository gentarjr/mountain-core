package com.mountain.entity.user;

import com.mountain.entity.role.Role;
import com.mountain.entity.role.Role.ERole;
import com.mountain.library.helper.CodecUtils;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
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

    @Column(name = "photo")
    private String photo;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "address")
    private String address;

    @Column(name = "email")
    private String email;

    @Column(name = "pin")
    private String pin;

    @Column(name = "confirmation_pin")
    private String confirmationPin;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private ERole role;

    @Column(name = "is_deleted")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean isDeleted;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_role", joinColumns = @JoinColumn(name = "id"))
    private Set<Role> roles;

    public User(){
        this.id = CodecUtils.generateUUID();
    }

    //user
    public User(String nik, String phoneNumber, String username, String firstName,
                String lastName, String address, String email, String pin, String confirmationPin, ERole role) {
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
        this.createdDate = LocalDateTime.now();
    }

    //ranger
    public User(String nik, String phoneNumber, String username, String firstName,
                String lastName, String address, String email, String pin, String confirmationPin, ERole role, Boolean isDeleted) {
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
        this.createdDate = LocalDateTime.now();
        this.isDeleted = isDeleted;
    }
}
