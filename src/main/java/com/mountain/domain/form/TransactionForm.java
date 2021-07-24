package com.mountain.domain.form;

import lombok.Data;

@Data
public class TransactionForm {

    private String mountain;
    private Double payment;
    private String status;
    private String reason;
    private String date;
    private String deviceId;
}
