package com.mountain.entity.detail;

import com.mountain.library.helper.CodecUtils;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "basecamp")
public class Basecamp implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Column(name = "mountain_id")
    private String mountainId;

    @Column(name = "basecamp_name")
    private String basecampName;

    @Column(name = "description")
    private String description;

    @Column(name = "photo")
    private String photo;

    @Column(name = "regulation")
    private String regulation;

    @Column(name = "full_address")
    private String fullAddress;

    @Column(name = "price")
    private Double price;

    @Column(name = "total_climber")
    private Integer totalClimber;

    @Column(name = "request_climber")
    private LocalDate requestClimber;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @Column(name = "updated_by")
    private String updatedBy;

    public Basecamp(){
        this.id = CodecUtils.generateUUID();
    }

    public Basecamp(String basecampName, String description, String regulation, String fullAddress, Double price) {
        this();
        this.basecampName = basecampName;
        this.description = description;
        this.regulation = regulation;
        this.fullAddress = fullAddress;
        this.price = price;
        this.createdDate = LocalDateTime.now();
    }
}
