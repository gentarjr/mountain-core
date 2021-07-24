package com.mountain.library.helper;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class StorageUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(StorageUtils.class);

    public static void saveFile(MultipartFile src, Path destPath, String fileName) throws IOException {
        File destFolder = destPath.toFile();
        Utils.mkdirIfNotExist(destFolder);

        File savedFile = Paths.get(destFolder.getAbsolutePath(), fileName).toFile();

        FileUtils.copyInputStreamToFile(src.getInputStream(), savedFile);
        String size = FileUtils.byteCountToDisplaySize(src.getSize());

        LOGGER.info("File {} ({}) saved to {}",
                savedFile.getName(), size, savedFile.getAbsolutePath());
    }

    public static void deleteFile(Path filePath, String fileName) throws IOException {
        File srcFolder = filePath.toFile();
        Utils.mkdirIfNotExist(srcFolder);

        File fileToDelete = Paths.get(srcFolder.getAbsolutePath(), fileName).toFile();
        if (fileToDelete.exists()) {
            LOGGER.info("Deleting file {}", fileToDelete.getName());
            FileUtils.forceDelete(fileToDelete);
        }
    }

    public static File getTempPicture(String id, String tempFolderPath) {
        final String[] imageExtensions = {"png", "PNG", "jpg", "JPG", "jpeg", "JPEG"};

        File file = null;
        for (String extension : imageExtensions) {
            file = Paths.get(tempFolderPath, id + "." + extension).toFile();
            if( file.exists() ) break;
        }

        return file;
    }
}
