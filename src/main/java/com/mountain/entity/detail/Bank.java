package com.mountain.entity.detail;

import com.mountain.library.helper.CodecUtils;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Data
public class Bank {

    @Id
    private String id;

    @Column(name = "mountain_id")
    private String mountainId;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "account_name")
    private String accountName;

    @Column(name = "account_number")
    private Integer accountNumber;

    @Column(name = "is_deleted")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean isDeleted;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @Column(name = "updated_by")
    private String updatedBy;

    public Bank(){
        this.id = CodecUtils.generateUUID();
    }

    public Bank(String bankName, String accountName, Integer accountNumber, String username) {
        this();
        this.bankName = bankName;
        this.accountName = accountName;
        this.accountNumber = accountNumber;
        this.createdBy = username;
        this.createdDate = LocalDateTime.now();
    }
}
