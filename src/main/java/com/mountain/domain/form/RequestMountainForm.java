package com.mountain.domain.form;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class RequestMountainForm implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer totalClimber;
    private String request;
    private String deviceId;
    private List<MemberForm> member;
    private EquipmentForm equipment;
    private TransactionForm transaction;

    private String firstName;
    private String status;
    private String reason;
    private String appId;
    private String appKey;

    public RequestMountainForm(String firstName, String status, String reason, String deviceId, String appId, String appKey) {
        this.firstName = firstName;
        this.status = status;
        this.reason = reason;
        this.deviceId = deviceId;
        this.appId = appId;
        this.appKey = appKey;
    }
}
