package com.mountain.controller.v1.mountain;


import com.mountain.dao.UserDao;
import com.mountain.domain.form.RequestMountainForm;
import com.mountain.domain.form.TransactionForm;
import com.mountain.domain.form.UserForm;
import com.mountain.entity.detail.App;
import com.mountain.entity.detail.RequestMountain;
import com.mountain.entity.detail.Transaction;
import com.mountain.entity.user.User;
import com.mountain.library.domain.ErrCode;
import com.mountain.library.domain.ResponseEnvelope;
import com.mountain.library.exceptions.NonexistentEntityException;
import com.mountain.library.exceptions.WinterfellException;
import com.mountain.library.helper.LoggerUtils;
import com.mountain.library.helper.ValidationUtils;
import com.mountain.repo.AppRepo;
import com.mountain.repo.RequestMountainRepo;
import com.mountain.repo.TransactionRepo;
import com.mountain.repo.UserRepo;
import com.mountain.service.internal.MountainServiceClient;
import com.mountain.service.mountain.MountainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping(value = "/v1/mountain")
@RequiredArgsConstructor
@Slf4j
public class MountainController {

    private final UserRepo userRepo;
    private final TransactionRepo transactionRepo;
    private final RequestMountainRepo requestMountainRepo;
    private final AppRepo appRepo;

    private final MountainServiceClient mountainServiceClient;

    private final UserDao userDao;

    private final MountainService mountainService;

    @GetMapping("/{id}/list-climber")
    public HttpEntity<ResponseEnvelope> listClimber(@PathVariable String id){
        ResponseEnvelope rm = new ResponseEnvelope(ErrCode.SUCCESS.getCode(), ErrCode.SUCCESS.getMessage());

        Map<String, Object> response = mountainService.listClimber(id);

        HttpStatus status = HttpStatus.OK;
        rm.setData(response);
        return ResponseEntity.status(status).body(rm);
    }

    @PutMapping(value = "/{id}/update-status/{climberId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpEntity<ResponseEnvelope> climberVerification(@PathVariable String id, @PathVariable String climberId,
                                                            @RequestBody UserForm form) {
        long start = System.currentTimeMillis();
        HttpStatus status = HttpStatus.OK;
        ErrCode errCode = ErrCode.SUCCESS;
        ResponseEnvelope rm = new ResponseEnvelope(errCode.getCode(), "Sukses");
        EntityManager em = userDao.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        Map<String, Object> result;
        try{
            String statusUser = form.getStatus();
            String reasonUser = form.getReason();

            User u = userRepo.findById(id);

            if(u == null || u.getRoleName().equals("")){
                throw new NonexistentEntityException(ErrCode.NO_CONTENT, "User not listed");
            }

            ValidationUtils.rejectIfEmptyField(" Cannot be empty",
                    new String[][]{{"Reason verification", reasonUser}, {"Status verification", statusUser}});

            App.AppCode nameApp = App.AppCode.MOUNTAIN;
            App app = appRepo.findByName(nameApp);

            RequestMountain requestMountain = requestMountainRepo.findById(climberId);

            transaction.begin();

            RequestMountain reqm = requestMountainRepo.findById(climberId);
            reqm.setStatus(statusUser);
            reqm.setReason(reasonUser);
            reqm.setUpdatedDate(LocalDateTime.now());
            reqm = em.merge(reqm);

            transaction.commit();

            result = mountainServiceClient.notificationUpdateStatusClimber(new RequestMountainForm(requestMountain.getUser().getFirstName(), statusUser, reasonUser, requestMountain.getDeviceId(), app.getAppId(), app.getAppKey()));

            log.info("verification status user {} success to input", u);
        }catch (WinterfellException e){
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

    @PutMapping(value = "/{id}/update-transaction/{transactionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpEntity<ResponseEnvelope> transactionVerification(@PathVariable String id, @PathVariable String transactionId,
                                                            @RequestBody TransactionForm form) {
        long start = System.currentTimeMillis();
        HttpStatus status = HttpStatus.OK;
        ErrCode errCode = ErrCode.SUCCESS;
        ResponseEnvelope rm = new ResponseEnvelope(errCode.getCode(), "Sukses");
        EntityManager em = userDao.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        Map<String, Object> result;
        try{
            String statusTransaction = form.getStatus();
            String reasonTransaction = form.getReason();

            User u = userRepo.findById(id);

            if(u == null || u.getRoleName().equals("")){
                throw new NonexistentEntityException(ErrCode.NO_CONTENT, "User not listed");
            }

            ValidationUtils.rejectIfEmptyField(" Cannot be empty",
                    new String[][]{{"Reason verification", statusTransaction}, {"Status verification", reasonTransaction}});

            Transaction tr = transactionRepo.findById(transactionId);

            RequestMountain requestMountain = requestMountainRepo.findByTransaction(tr);

            if(requestMountain.getStatus().equals("VERIFIED")){
                throw new NonexistentEntityException(ErrCode.UNUSED, "User has not verified");
            }

            App.AppCode nameApp = App.AppCode.MOUNTAIN;
            App app = appRepo.findByName(nameApp);

            transaction.begin();

            tr.setStatus(statusTransaction);
            tr.setReason(reasonTransaction);
            tr.setUpdatedDate(LocalDateTime.now());
            tr = em.merge(tr);

            transaction.commit();

            result = mountainServiceClient.notificationUpdateStatusTransaction(new RequestMountainForm(requestMountain.getUser().getFirstName(), statusTransaction, reasonTransaction, requestMountain.getDeviceId(), app.getAppId(), app.getAppKey()));

            log.info("verification status user {} success to input", u);
        }catch (WinterfellException e){
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
