package com.mountain.entity.user;

import com.mountain.library.helper.CodecUtils;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@Entity
public class Status {

    @Id
    private String id;

    @Column(name = "users_id")
    private String usersId;

    @Column(name = "mountain_id")
    private String mountainId;

    @Column(name = "username")
    private String username;

    @Column(name = "role")
    private String role;

    @Column(name = "status")
    private String status;

    @Column(name = "photo")
    private String photo;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    public Status(){
        this.id = CodecUtils.generateUUID();
    }

    public Status(String username, String role, String status) {
        this();
        this.username = username;
        this.role = role;
        this.status = status;
        this.createdDate = LocalDateTime.now();
    }
}
