package com.mountain.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mountain.entity.detail.Equipment;
import com.mountain.library.helper.DateUtils;
import lombok.Data;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class EquipmentResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private Integer backPack;
    private Integer flashLight;
    private Integer food;
    private Integer mattress;
    private Integer nesting;
    private Integer rainCoat;
    private Integer stove;
    private Integer tent;
    private Integer water;
    private String createdDate;
    private String updatedDate;

    public EquipmentResponse() {

    }

    public EquipmentResponse(Equipment e) {
        if (e != null){
            this.id = e.getId();
            this.backPack = e.getBackPack();
            this.flashLight = e.getFlashLight();
            this.food = e.getFood();
            this.mattress = e.getMattress();
            this.nesting = e.getNesting();
            this.rainCoat = e.getRainCoat();
            this.stove = e.getStove();
            this.tent = e.getTent();
            this.water = e.getWater();

            this.createdDate = DateUtils.formatDateTimeOrEmptyString(e.getCreatedDate());
            this.updatedDate = DateUtils.formatDateTimeOrEmptyString(e.getUpdatedDate(), "dd/MMM/yyyy kk:mm");
        }
    }
}
