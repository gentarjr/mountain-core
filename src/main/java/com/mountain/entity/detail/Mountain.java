package com.mountain.entity.detail;

import com.mountain.library.helper.CodecUtils;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
public class Mountain implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Column(name = "photo")
    private String photo;

    @Column(name = "mountain_name")
    private String mountainName;

    @Column(name = "description")
    private String description;

    @Column(name = "height")
    private Integer height;

    @Column(name = "fullAddress")
    private String fullAddress;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @Column(name = "updated_by")
    private String updatedBy;

    public Mountain(){
        this.id = CodecUtils.generateUUID();
    }

    public Mountain(String mountainName, String description, Integer height,
                    String fullAddress, String createdBy) {
        this();
        this.mountainName = mountainName;
        this.description = description;
        this.height = height;
        this.fullAddress = fullAddress;
        this.createdDate = LocalDateTime.now();
        this.createdBy = createdBy;
    }
}
