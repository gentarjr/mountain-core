package com.mountain.dao;

import com.mountain.entity.detail.Basecamp;
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
public class BasecampDao extends AbstractDao implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(StatusDao.class);

    public void create(Basecamp basecamp) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(basecamp);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (findBasecamp(basecamp.getId()) != null) {
                throw new PreexistingEntityException("Basecamp " + basecamp + " already exists.", e);
            }
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Basecamp basecamp) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            basecamp = em.merge(basecamp);
            em.getTransaction().commit();
        } catch (Exception e) {
            String msg = e.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = basecamp.getId();
                if (findBasecamp(id) == null) {
                    throw new NonexistentEntityException("The Basecamp with id " + id + " no longer exists");
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
            Basecamp basecamp;
            try {
                basecamp = em.getReference(Basecamp.class, id);
                basecamp.getId();
            } catch (EntityNotFoundException ex) {
                throw new NonexistentEntityException("The Basecamp with id " + id + " no longer exists.", ex);
            }
            em.remove(basecamp);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public Basecamp findBasecamp(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Basecamp.class, id);
        } finally {
            em.close();
        }
    }

    public void saveDocBaseCamp(String folder, Basecamp basecamp, MultipartFile photoFile) throws IOException {
        String newPhoto = photoFile.getOriginalFilename();

        File savedFile = Paths.get(folder, newPhoto).toFile();

        InputStream inputPhoto = photoFile.getInputStream();

        FileUtils.copyInputStreamToFile(inputPhoto, savedFile);
        String Photo = FileUtils.byteCountToDisplaySize(photoFile.getSize());

        logger.info("Uploaded {} saved to {} {} {}", basecamp.getPhoto(),
                savedFile.getName(), Photo, savedFile.getAbsolutePath());
    }
}
