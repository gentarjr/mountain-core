package com.mountain.entity.detail;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class App {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private AppCode name;

    private String appId;

    private String appKey;

    public App() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAppId() {
        return appId;
    }

    public String getAppKey() {
        return appKey;
    }

    public AppCode getName() {
        return name;
    }

    public void setName(AppCode name) {
        this.name = name;
    }

    public enum AppCode {
        MOUNTAIN
    }
}
