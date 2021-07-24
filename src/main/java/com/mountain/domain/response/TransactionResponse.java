package com.mountain.domain.response;

import lombok.Data;

@Data
public class TransactionResponse {

    private String id;
    private String mountain;
    private Double payment;
    private String date;
    private String status;
    private String reason;
    private String createdDate;
    private String updatedDate;
}
