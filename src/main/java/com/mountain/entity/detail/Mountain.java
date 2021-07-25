package com.mountain.entity.detail;

import com.mountain.library.helper.CodecUtils;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Data
public class Mountain implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Column(name = "mountain_name")
    private String mountainName;

    @Column(name = "basecamp_name")
    private String basecampName;

    @Column(name = "description")
    private String description;

    @Column(name = "height")
    private Integer height;

    @Column(name = "regulation")
    private String regulation;

    @Column(name = "fullAddress")
    private String fullAddress;

    @Column(name = "price")
    private Double price;

    @Column(name = "created_date")
    private Timestamp createdDate;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_date")
    private Timestamp updatedDate;

    @Column(name = "updated_by")
    private String updatedBy;

    public Mountain(){
        this.id = CodecUtils.generateUUID();
    }

    public Mountain(String mountainName, String basecampName, String description, Integer height,
                    String regulation, String fullAddress, Double price, Timestamp createdDate, String createdBy) {
        this();
        this.mountainName = mountainName;
        this.basecampName = basecampName;
        this.description = description;
        this.height = height;
        this.regulation = regulation;
        this.fullAddress = fullAddress;
        this.price = price;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
    }
}
