package com.mountain.controller.v1.user;

import com.mountain.controller.v1.mountain.MountainController;
import com.mountain.dao.StatusDao;
import com.mountain.dao.UserDao;
import com.mountain.domain.form.UserForm;
import com.mountain.entity.user.ReplyStatus;
import com.mountain.entity.user.Status;
import com.mountain.entity.user.User;
import com.mountain.library.domain.AppConstant;
import com.mountain.library.domain.ErrCode;
import com.mountain.library.domain.ResponseEnvelope;
import com.mountain.library.exceptions.NonexistentEntityException;
import com.mountain.library.exceptions.WinterfellException;
import com.mountain.library.helper.LoggerUtils;
import com.mountain.library.helper.ValidationUtils;
import com.mountain.repo.MountainRepo;
import com.mountain.repo.ReplyStatusRepo;
import com.mountain.repo.StatusRepo;
import com.mountain.repo.UserRepo;
import com.mountain.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@RestController
@RequestMapping(value = "/v1/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(MountainController.class);

    private final UserService userService;

    private final StatusRepo statusRepo;
    private final UserRepo userRepo;
    private final MountainRepo mountainRepo;
    private final ReplyStatusRepo replyStatusRepo;

    private final UserDao userDao;
    private final StatusDao statusDao;

    @PostMapping("/input-status/{id}")
    public HttpEntity<ResponseEnvelope> inputStatus(@PathVariable String id, UserForm form,
                                                    @RequestParam("photo") MultipartFile photo) {
        long start = System.currentTimeMillis();
        HttpStatus status = HttpStatus.OK;
        ErrCode errCode = ErrCode.SUCCESS;
        ResponseEnvelope rm = new ResponseEnvelope(errCode.getCode(), "Success");

        EntityManager em = statusDao.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            String photoNew = photo.getOriginalFilename();
            String statusUser = form.getStatus();
            String mountainId = form.getMountainId();

            ValidationUtils.rejectIfEmptyField(" Cannot be empty",
                    new String[][]{{"Status", statusUser}});

            User u = userDao.findUser(id);

            if (u == null) {
                throw new NonexistentEntityException(ErrCode.NO_CONTENT, "User not listed");
            }

            String role = u.getRole().toString();

            Path folderStatusPath;


            Status s = new Status(u.getUsername(), role, statusUser);
            s.setUsersId(id);

            if (!mountainId.equals("")) {
                s.setMountainId(mountainId);
            }

            if (!photo.isEmpty()) {
                s.setPhoto(photoNew);
                folderStatusPath = Paths.get(AppConstant.BASE_FOLDER_PATH, AppConstant.FOLDER_USER_PATH
                        + "/" + id, "/status/" + s.getId());
                File folderMountain = folderStatusPath.toFile();
                if (!folderMountain.exists() || !folderMountain.isDirectory()) {
                    folderMountain.mkdir();
                    logger.info("User folder didn't exist, create one at {}.", folderMountain.getAbsolutePath());
                }
                statusDao.saveDocStatus(folderStatusPath.toString(), s, photo);
            }
            s = statusRepo.save(s);

            log.info("success to input status {}", u.getUsername());
        } catch (WinterfellException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            status = HttpStatus.CONFLICT;
            rm.setCode(e.getErrCode().getCode());
            rm.setMessage(e.getMessage());
            log.warn("Exception Caught :", e);
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            rm.setCode(ErrCode.BAD_REQUEST.getCode());
            rm.setMessage(ErrCode.BAD_REQUEST.getMessage());

            log.warn("Exception Caught :", e);
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }

        long end = System.currentTimeMillis();
        LoggerUtils.logTime(log, Thread.currentThread().getStackTrace()[1].getMethodName(), start, end);
        return ResponseEntity.status(status).body(rm);
    }

    @GetMapping("/list-status/{id}")
    public HttpEntity<ResponseEnvelope> listStatus(@PathVariable String id) {
        ResponseEnvelope rm = new ResponseEnvelope(ErrCode.SUCCESS.getCode(), ErrCode.SUCCESS.getMessage());

        Map<String, Object> response = userService.listStatus(id);

        HttpStatus status = HttpStatus.OK;
        rm.setData(response);
        return ResponseEntity.status(status).body(rm);
    }

    @GetMapping("/list-status-user/{id}")
    public HttpEntity<ResponseEnvelope> listStatusUser(@PathVariable String id) {
        ResponseEnvelope rm = new ResponseEnvelope(ErrCode.SUCCESS.getCode(), ErrCode.SUCCESS.getMessage());

        Map<String, Object> response = userService.listStatusUser(id);

        HttpStatus status = HttpStatus.OK;
        rm.setData(response);
        return ResponseEntity.status(status).body(rm);
    }

    @PostMapping("/input-reply-status/{id}")
    public HttpEntity<ResponseEnvelope> inputReplyStatus(@PathVariable String id, @RequestBody UserForm form) {
        long start = System.currentTimeMillis();
        HttpStatus status = HttpStatus.OK;
        ErrCode errCode = ErrCode.SUCCESS;
        ResponseEnvelope rm = new ResponseEnvelope(errCode.getCode(), "Success");

        EntityManager em = statusDao.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            String replyStatus = form.getReplyStatus();
            String statusId = form.getStatusId();

            ValidationUtils.rejectIfEmptyField(" Cannot be empty",
                    new String[][]{{"Status", replyStatus}, {"Status Id", statusId}});

            User u = userDao.findUser(id);

            if (u == null) {
                throw new NonexistentEntityException(ErrCode.NO_CONTENT, "User not listed");
            }

            String role = u.getRole().toString();

            ReplyStatus rs = new ReplyStatus(statusId, u.getUsername(), role, replyStatus);

            rs = replyStatusRepo.save(rs);

            log.info("reply status success {}", u.getId());
        } catch (WinterfellException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            status = HttpStatus.CONFLICT;
            rm.setCode(e.getErrCode().getCode());
            rm.setMessage(e.getMessage());
            log.warn("Exception Caught :", e);
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            rm.setCode(ErrCode.BAD_REQUEST.getCode());
            rm.setMessage(ErrCode.BAD_REQUEST.getMessage());

            log.warn("Exception Caught :", e);
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }

        long end = System.currentTimeMillis();
        LoggerUtils.logTime(log, Thread.currentThread().getStackTrace()[1].getMethodName(), start, end);
        return ResponseEntity.status(status).body(rm);
    }
}
