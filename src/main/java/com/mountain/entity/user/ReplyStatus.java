package com.mountain.entity.user;

import com.mountain.library.helper.CodecUtils;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

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
    private LocalDateTime createdDate;

    public ReplyStatus(){
        this.id = CodecUtils.generateUUID();
    }

    public ReplyStatus(String statusId, String username, String role, String replyStatus) {
        this();
        this.statusId = statusId;
        this.username = username;
        this.role = role;
        this.reply = replyStatus;
        this.createdDate = LocalDateTime.now();
    }
}
