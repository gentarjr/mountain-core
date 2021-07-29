package com.mountain.entity.detail;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
public class RequestMountain implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Column(name = "users_id")
    private String usersId;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "equipment_id")
    private String equipmentId;

    @Column(name = "tracking_id")
    private String trackingId;

    @Column(name = "total_climber")
    private Integer totalClimber;

    @Column(name = "request_mountain")
    private String requestMountain;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "reason")
    private String reason;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

}
