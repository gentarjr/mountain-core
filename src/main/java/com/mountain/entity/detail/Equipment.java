package com.mountain.entity.detail;

import com.mountain.library.helper.CodecUtils;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
public class Equipment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    private Integer backPack;

    private Integer water;

    private Integer mattress;

    private Integer tent;

    private Integer food;

    private Integer stove;

    private Integer nesting;

    private Integer rainCoat;

    private Integer flashLight;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    public Equipment(){

    }

    public Equipment(Integer backPack, Integer water, Integer mattress, Integer tent, Integer food,
                     Integer stove, Integer nesting, Integer rainCoat, Integer flashLight) {
        this.id = CodecUtils.generateUUID();
        this.backPack = backPack;
        this.water = water;
        this.mattress = mattress;
        this.tent = tent;
        this.food = food;
        this.stove = stove;
        this.nesting = nesting;
        this.rainCoat = rainCoat;
        this.flashLight = flashLight;
        this.createdDate = LocalDateTime.now();
    }
}
