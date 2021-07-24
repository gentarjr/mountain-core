package com.mountain.entity.detail;

import com.mountain.library.helper.CodecUtils;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
public class Member implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    private String idCard;

    private String phoneNumber;

    private String name;

    private String provinsi;

    private String chairman;

    private String kota;

    private String kecamatan;

    private String rtRw;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id")
    private RequestMountain requestMountain;

    public Member(){

    }

    public Member(String idCard, String name, String phoneNumber,
                  String provinsi, String kota, String kecamatan, String rtrw) {
        this.id = CodecUtils.generateUUID();
        this.idCard = idCard;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.provinsi = provinsi;
        this.kota = kota;
        this.kecamatan = kecamatan;
        this.rtRw = rtrw;
        this.createdDate = LocalDateTime.now();
    }
}
