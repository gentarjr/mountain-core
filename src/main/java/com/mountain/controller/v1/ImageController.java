package com.mountain.controller.v1;

import com.mountain.dao.MountainDao;
import com.mountain.entity.detail.Mountain;
import com.mountain.library.domain.AppConstant;
import com.mountain.library.domain.ErrCode;
import com.mountain.library.domain.ResponseEnvelope;
import com.mountain.library.exceptions.NonexistentEntityException;
import com.mountain.library.helper.LoggerUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Paths;

@RestController
@RequestMapping("/v1/img")
@RequiredArgsConstructor

public class ImageController {

    private final Logger logger = LoggerFactory.getLogger(ImageController.class);

    private final MountainDao mountainDao;

    @GetMapping("/{id}/mountain")
    @ResponseBody
    public ResponseEntity<byte[]> getPhotoMountain(
            HttpServletRequest request, HttpServletResponse response,
            @PathVariable() String id) {

        long start = System.currentTimeMillis();

        HttpStatus status = HttpStatus.OK;
        ErrCode errCode = ErrCode.SUCCESS;

        ResponseEnvelope rm = new ResponseEnvelope(errCode.getCode(), "Sukses");
        try {
            Mountain m = mountainDao.findMountain(id);

            if (m == null) {
                throw new NonexistentEntityException(ErrCode.NO_CONTENT, "Mountain image not listed");
            }

            HttpHeaders headers = new HttpHeaders();
            String picture = m.getPhoto();
            if (picture == null) {
                status = HttpStatus.NOT_FOUND;
                return ResponseEntity.status(status).body(new byte[0]);
            }
            File file = Paths.get(AppConstant.BASE_FOLDER_PATH, AppConstant.FOLDER_MOUNTAIN_PATH,
                    m.getId(), picture).toFile();

            InputStream in = new FileInputStream(file);
            byte[] media = IOUtils.toByteArray(in);
            in.close();
            headers.setCacheControl(CacheControl.noCache().getHeaderValue());
            String ext = FilenameUtils.getExtension(m.getPhoto());
            switch (ext) {
                case "png":
                case "PNG":
                    headers.setContentType(MediaType.IMAGE_PNG);
                    break;
                case "jpg":
                case "JPG":
                case "jpeg":
                case "JPEG":
                default:
                    headers.setContentType(MediaType.IMAGE_JPEG);
            }
            logger.info("User [{}] accessing Product Photo {}.", m.getId(), m.getPhoto());
            long end = System.currentTimeMillis();
            LoggerUtils.logTime(
                    logger, Thread.currentThread().getStackTrace()[1].getMethodName(),
                    start, end);

            return new ResponseEntity<>(media, headers, status);
        } catch (NonexistentEntityException ex) {
            status = HttpStatus.CONFLICT;

            rm.setCode(ex.getErrCode().getCode());
            rm.setMessage(ex.getMessage());
            logger.warn("Exception Caught :", ex);

        } catch (FileNotFoundException ex) {
            status = HttpStatus.NOT_FOUND;

            rm.setCode(ErrCode.BAD_REQUEST.getCode());
            rm.setMessage(ErrCode.BAD_REQUEST.getMessage());
            logger.warn("Exception Caught :", ex);

        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;

            rm.setCode(ErrCode.BAD_REQUEST.getCode());
            rm.setMessage(ErrCode.BAD_REQUEST.getMessage());

            logger.warn("Exception Caught :", ex);

        }
        long end = System.currentTimeMillis();
        LoggerUtils.logTime(
                logger, Thread.currentThread().getStackTrace()[1].getMethodName(),
                start, end);

        return ResponseEntity.status(status).body(new byte[0]);
    }
}
