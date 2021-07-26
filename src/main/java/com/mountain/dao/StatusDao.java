package com.mountain.dao;

import com.mountain.entity.user.Status;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Paths;

@Service
public class StatusDao extends AbstractDao implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(StatusDao.class);

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
