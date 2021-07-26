package com.mountain.entity.user;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
public class ReplyStatus {

    @Id
    private String id;

    @Column(name = "status_id")
    private String statusId;

    @Column(name = "username")
    private String username;

    @Column(name = "role")
    private String role;

    @Column(name = "reply")
    private String reply;

    @Column(name = "created_date")
    private Timestamp createdDate;

    @Column(name = "updated_date")
    private Timestamp updatedDate;
}
