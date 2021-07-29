package com.mountain.domain.form;

import lombok.Data;

@Data
public class BankForm {

    private String mountainId;
    private String bankName;
    private String accountName;
    private Integer accountNumber;
    private Boolean isDeleted;
}
