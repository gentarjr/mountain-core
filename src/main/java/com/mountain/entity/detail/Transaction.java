package com.mountain.entity.detail;

import com.mountain.library.helper.CodecUtils;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
public class Transaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    private String mountain;

    private Double payment;

    private String climberDate;

    private String status;

    private String reason;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    public Transaction(){

    }

    public Transaction(String mountain, Double payment, String stats, String reas, String date) {
        this.id = CodecUtils.generateUUID();
        this.mountain = mountain;
        this.payment = payment;
        this.status = stats;
        this.reason = reas;
        this.climberDate = date;
        this.createdDate = LocalDateTime.now();
    }
}
