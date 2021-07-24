package com.mountain.library.domain;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum ConfigGroup {
    DEFAULT("Default"),
    INSTALLATION("Installasi"),
    APP("App"),
    TOPUP("Topup"),
    BALANCE_WITHDRAWAL("Balance Withdrawal"),
    GV("Gudang voucher"),
    FLIP("Flip"),
    DOKU_EMONEY("Doku emoney"),
    PPOB_DOKU("PPOB Simple Doku"),
    DOKU_MERCHANT("Doku merchant"),
    SHIPPING("Shipping"),
    REKBER("Rekber"),
    NOTIFICATION("Notifikasi"),
    ALARM("Alarm"),
    CMS("Content Management System"),
    QRIS("Cash-It QRIS Service"),
    AGRATEK_PROXY("Agratek Payment Proxy Service"),
    FLICKSHOP("FlickShop");

    private String deskripsi;

    private ConfigGroup(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getDeskripsi() {
        return this.deskripsi;
    }

    public static List<String> toList() {
        return (List) Stream.of(values()).map((s) -> {
            return s.name();
        }).collect(Collectors.toList());
    }

    public static String[] toArray() {
        List<String> toList = toList();
        return (String[])toList.toArray(new String[1]);
    }

    public static List<ConfigGroup> toEnumList(String... status) {
        return (List)Stream.of(status).map((s) -> {
            return valueOf(s);
        }).collect(Collectors.toList());
    }

    public String toString() {
        return "ConfigGroup{deskripsi=" + this.deskripsi + "}";
    }
}
