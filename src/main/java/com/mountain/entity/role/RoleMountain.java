package com.mountain.entity.role;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class RoleMountain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String roleName;

    public RoleMountain(){

    }
    public RoleMountain(String rolesName) {
        this.roleName = rolesName;
    }
}
