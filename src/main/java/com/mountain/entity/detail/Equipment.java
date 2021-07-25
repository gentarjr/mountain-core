package com.mountain.entity.detail;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
public class Equipment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Column(name = "back_pack")
    private Integer backPack;

    @Column(name = "water")
    private Integer water;

    @Column(name = "mattress")
    private Integer mattress;

    @Column(name = "tent")
    private Integer tent;

    @Column(name = "food")
    private Integer food;

    @Column(name = "stove")
    private Integer stove;

    @Column(name = "nesting")
    private Integer nesting;

    @Column(name = "rain_coat")
    private Integer rainCoat;

    @Column(name = "flash_light")
    private Integer flashLight;

    @Column(name = "created_date")
    private Timestamp createdDate;

    @Column(name = "updated_date")
    private Timestamp updatedDate;

}
