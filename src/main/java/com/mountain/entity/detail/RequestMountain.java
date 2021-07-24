package com.mountain.entity.detail;

import com.mountain.entity.user.User;
import com.mountain.library.helper.CodecUtils;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class RequestMountain implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    private Integer totalClimber;

    private String requestMountain;

    private String status;

    private String reason;

    private String deviceId;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ToString.Exclude
    @OneToMany(mappedBy = "requestMountain")
    private List<Member> member;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "equipment_id")
    private Equipment equipment;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;

    public RequestMountain(){

    }

    public RequestMountain(Integer totalClimber, String requestMountain, String status, String reason, String deviceId) {
        this.id = CodecUtils.generateUUID();
        this.totalClimber = totalClimber;
        this.requestMountain = requestMountain;
        this.status = status;
        this.reason = reason;
        this.deviceId = deviceId;
        this.createdDate = LocalDateTime.now();
    }
}
