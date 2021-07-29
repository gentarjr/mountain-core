package com.mountain.dao;

import com.mountain.entity.user.ReplyStatus;
import com.mountain.library.exceptions.NonexistentEntityException;
import com.mountain.library.exceptions.PreexistingEntityException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.io.Serializable;

@Service
public class ReplyStatusDao extends AbstractDao implements Serializable {

    private static final long serialVersionUID = 1L;

    public void create(ReplyStatus replyStatus) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(replyStatus);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (findReplyStatus(replyStatus.getId()) != null) {
                throw new PreexistingEntityException("Reply status " + replyStatus + " already exists.", e);
            }
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ReplyStatus replyStatus) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            replyStatus = em.merge(replyStatus);
            em.getTransaction().commit();
        } catch (Exception e) {
            String msg = e.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = replyStatus.getId();
                if (findReplyStatus(id) == null) {
                    throw new NonexistentEntityException("The Reply status with id " + id + " no longer exists");
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
            ReplyStatus replyStatus;
            try {
                replyStatus = em.getReference(ReplyStatus.class, id);
                replyStatus.getId();
            } catch (EntityNotFoundException ex) {
                throw new NonexistentEntityException("The Reply status with id " + id + " no longer exists.", ex);
            }
            em.remove(replyStatus);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public ReplyStatus findReplyStatus(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ReplyStatus.class, id);
        } finally {
            em.close();
        }
    }
}
