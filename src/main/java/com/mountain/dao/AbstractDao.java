package com.mountain.dao;

import com.mountain.library.domain.ConfigMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public abstract class AbstractDao {
    protected ConfigMap configs;

    @Autowired
    private EntityManagerFactory emf;

    public EntityManager getEntityManager() {
        EntityManager em;
        em = emf.createEntityManager();
        return em;
    }

    @ModelAttribute("configs")
    public ConfigMap getConfigs() {
        return configs;
    }
}
