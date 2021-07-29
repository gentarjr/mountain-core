package com.mountain.entity.detail;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class RequestMountain implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Column(name = "users_id")
    private String usersId;

    @Column(name = "basecamp_id")
    private String basecampId;

    @Column(name = "equipment_id")
    private String equipmentId;

    @Column(name = "tracking_id")
    private String trackingId;

    @Column(name = "total_climber")
    private Integer totalClimber;

    @Column(name = "mountain_name")
    private String mountainName;

    @Column(name = "tracking_status")
    private Integer trackingStatus;

    @Column(name = "price")
    private Double price;

    @Column(name = "payment")
    private Double payment;

    @Column(name = "account_name")
    private String accountName;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "photo_payment")
    private String photoPayment;

    @Column(name = "name_payment_user")
    private String namePaymentUser;

    @Column(name = "bank_payment_user")
    private String bank_payment_user;

    @Column(name = "climber_date")
    private LocalDate climberDate;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

}
