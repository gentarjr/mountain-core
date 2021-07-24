package com.mountain.dao;

import com.mountain.entity.user.User;
import com.mountain.library.exceptions.NonexistentEntityException;
import com.mountain.library.exceptions.PreexistingEntityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.io.Serializable;

@Service
public class UserDao extends AbstractDao implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(UserDao.class);

    public void create(User user) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (findUser(user.getId()) != null) {
                throw new PreexistingEntityException("Bank " + user + " already exists.", e);
            }
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(User user) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            user = em.merge(user);
            em.getTransaction().commit();
        } catch (Exception e) {
            String msg = e.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = user.getId();
                if (findUser(id) == null) {
                    throw new NonexistentEntityException("The Bank with id " + id + " no longer exists");
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
            User user;
            try {
                user = em.getReference(User.class, id);
                user.getId();
            } catch (EntityNotFoundException ex) {
                throw new NonexistentEntityException("The Bank with id " + id + " no longer exists.", ex);
            }
            em.remove(user);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public User findUser(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(User.class, id);
        } finally {
            em.close();
        }
    }
}
