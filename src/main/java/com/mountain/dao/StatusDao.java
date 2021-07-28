package com.mountain.dao;

import com.mountain.entity.user.Status;
import com.mountain.library.exceptions.NonexistentEntityException;
import com.mountain.library.exceptions.PreexistingEntityException;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Paths;

@Service
public class StatusDao extends AbstractDao implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(StatusDao.class);

    public void create(Status status) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(status);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (findStatus(status.getId()) != null) {
                throw new PreexistingEntityException("Status " + status + " already exists.", e);
            }
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Status status) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            status = em.merge(status);
            em.getTransaction().commit();
        } catch (Exception e) {
            String msg = e.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = status.getId();
                if (findStatus(id) == null) {
                    throw new NonexistentEntityException("The Status with id " + id + " no longer exists");
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
            Status status;
            try {
                status = em.getReference(Status.class, id);
                status.getId();
            } catch (EntityNotFoundException ex) {
                throw new NonexistentEntityException("The User with id " + id + " no longer exists.", ex);
            }
            em.remove(status);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public Status findStatus(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Status.class, id);
        } finally {
            em.close();
        }
    }

    public void saveDocStatus(String folder, Status status, MultipartFile photoFile) throws IOException {
        String newPhoto = photoFile.getOriginalFilename();

        File savedFile = Paths.get(folder, newPhoto).toFile();

        InputStream inputPhoto = photoFile.getInputStream();

        FileUtils.copyInputStreamToFile(inputPhoto, savedFile);
        String Photo = FileUtils.byteCountToDisplaySize(photoFile.getSize());

        logger.info("Uploaded {} saved to {} {} {}", status.getPhoto(),
                savedFile.getName(), Photo, savedFile.getAbsolutePath());
    }
}
