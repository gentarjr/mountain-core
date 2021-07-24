package com.mountain.domain.response;

import lombok.Data;

import java.util.List;

@Data
public class RequestMountainResponse {

    private String id;
    private Integer totalClimber;
    private String requestMountain;
    private List<MemberResponse> member;
    private EquipmentResponse equipment;
    private TransactionResponse transactions;
    private String createdDate;
    private String updatedDate;
}
