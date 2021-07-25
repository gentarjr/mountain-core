package com.mountain.dao;

import com.mountain.entity.detail.Mountain;
import com.mountain.library.exceptions.NonexistentEntityException;
import com.mountain.library.exceptions.PreexistingEntityException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.io.Serializable;

@Service
public class MountainDao extends AbstractDao implements Serializable {

    private static final long serialVersionUID = 1L;

    public void create(Mountain mountain) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(mountain);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (findMountain(mountain.getId()) != null) {
                throw new PreexistingEntityException("Mountain " + mountain + " already exists.", e);
            }
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Mountain mountain) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            mountain = em.merge(mountain);
            em.getTransaction().commit();
        } catch (Exception e) {
            String msg = e.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = mountain.getId();
                if (findMountain(id) == null) {
                    throw new NonexistentEntityException("The Mountain with id " + id + " no longer exists");
                }
            }
            throw e;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Mountain mountain;
            try {
                mountain = em.getReference(Mountain.class, id);
                mountain.getId();
            } catch (EntityNotFoundException ex) {
                throw new NonexistentEntityException("The Mountain with id " + id + " no longer exists.", ex);
            }
            em.remove(mountain);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public Mountain findMountain(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Mountain.class, id);
        } finally {
            em.close();
        }
    }
}
