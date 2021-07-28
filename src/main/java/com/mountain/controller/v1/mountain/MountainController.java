package com.mountain.controller.v1.mountain;

import com.mountain.dao.BasecampDao;
import com.mountain.dao.MountainDao;
import com.mountain.dao.UserDao;
import com.mountain.domain.form.BasecampForm;
import com.mountain.domain.form.MountainForm;
import com.mountain.domain.form.UserForm;
import com.mountain.entity.detail.Basecamp;
import com.mountain.entity.detail.Mountain;
import com.mountain.entity.role.Role;
import com.mountain.entity.user.User;
import com.mountain.library.domain.AppConstant;
import com.mountain.library.domain.ErrCode;
import com.mountain.library.domain.ResponseEnvelope;
import com.mountain.library.exceptions.InvalidFieldException;
import com.mountain.library.exceptions.NonexistentEntityException;
import com.mountain.library.exceptions.PreexistingUserException;
import com.mountain.library.exceptions.WinterfellException;
import com.mountain.library.helper.CodecUtils;
import com.mountain.library.helper.LoggerUtils;
import com.mountain.library.helper.ValidationUtils;
import com.mountain.repo.BasecampRepo;
import com.mountain.repo.MountainRepo;
import com.mountain.repo.RoleRepo;
import com.mountain.repo.UserRepo;
import com.mountain.service.mountain.MountainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping(value = "/v1/mountain")
@Slf4j
@RequiredArgsConstructor
public class MountainController {

    private final Logger logger = LoggerFactory.getLogger(MountainController.class);

    private final UserDao userDao;
    private final MountainRepo mountainRepo;
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final BasecampRepo basecampRepo;

    private final MountainDao mountainDao;
    private final BasecampDao basecampDao;

    private final MountainService mountainService;

    @PostMapping("/input-mountain/{id}")
    public HttpEntity<ResponseEnvelope> inputMountain(MountainForm form, @PathVariable String id, @RequestParam("photo") MultipartFile photo) {

        long start = System.currentTimeMillis();
        HttpStatus status = HttpStatus.OK;
        ErrCode errCode = ErrCode.SUCCESS;
        ResponseEnvelope rm = new ResponseEnvelope(errCode.getCode(), "Success");

        EntityManager em = userDao.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            User u = userDao.findUser(id);

            String mountainName = form.getMountainName();
            String description = form.getDescription();
            Integer height = form.getHeight();
            String fullAddress = form.getFullAddress();

            String photoNew = photo.getOriginalFilename();

            if (u == null) {
                throw new NonexistentEntityException(ErrCode.NOT_ACCEPTABLE, "User not listed");
            }

            String role = u.getRole().toString();
            if (!role.equals("SYSADMIN")) {
                throw new NonexistentEntityException(ErrCode.NOT_ACCEPTABLE, "User not accept");
            }

            if (!mountainName.equals(u.getLastName())) {
                throw new NonexistentEntityException(ErrCode.NOT_ACCEPTABLE, "Mountain name is wrong");
            }

            ValidationUtils.rejectIfEmptyField(" cannot be empty",
                    new String[][]{{"Mountain name", mountainName},
                            {"Description", description}, {"Full Address", fullAddress}});

            Mountain m = mountainRepo.findByMountainName(mountainName);

            if (m != null) {
                throw new NonexistentEntityException(ErrCode.NOT_ACCEPTABLE, "Mountain already registered");
            }

            Mountain mountain = new Mountain(mountainName, description, height, fullAddress, u.getUsername());
            mountain.setCreatedBy(u.getUsername());

            if (!photoNew.equals("")) {
                mountain.setPhoto(photoNew);
                Path folderMountainPath = Paths.get(AppConstant.BASE_FOLDER_PATH, AppConstant.FOLDER_MOUNTAIN_PATH, mountain.getId());
                File folderMountain = folderMountainPath.toFile();
                if (!folderMountain.exists() || !folderMountain.isDirectory()) {
                    folderMountain.mkdir();
                    logger.info("User folder didn't exist, create one at {}.", folderMountain.getAbsolutePath());
                }
                mountainDao.saveDocMountain(folderMountainPath.toString(), mountain, photo);
            }
            mountainRepo.save(mountain);

            u.setMountainId(mountain.getId());
            u = userRepo.save(u);

            log.info("input mountain from admin {} success", u.getUsername());
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
        LoggerUtils.logTime(log, Thread.currentThread().
                getStackTrace()[1].
                getMethodName(), start, end);
        return ResponseEntity.status(status).
                body(rm);
    }

    @PutMapping("/update-mountain/{id}")
    public HttpEntity<ResponseEnvelope> updateMountain(@RequestBody MountainForm form, @PathVariable String id) {
        long start = System.currentTimeMillis();
        HttpStatus status = HttpStatus.OK;
        ErrCode errCode = ErrCode.SUCCESS;
        ResponseEnvelope rm = new ResponseEnvelope(errCode.getCode(), "Success");

        EntityManager em = mountainDao.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            String mountainId = form.getMountainId();
            String description = form.getDescription();

            User u = userDao.findUser(id);

            if (u == null) {
                throw new NonexistentEntityException(ErrCode.NOT_ACCEPTABLE, "User not listed");
            }

            String role = u.getRole().toString();
            if (!role.equals("SYSADMIN")) {
                throw new NonexistentEntityException(ErrCode.NOT_ACCEPTABLE, "User not accept");
            }

            ValidationUtils.rejectIfEmptyField(" cannot be empty",
                    new String[][]{{"Description", description}});

            Mountain m = mountainDao.findMountain(mountainId);

            m.setDescription(description);
            m.setUpdatedBy(u.getUsername());
            m.setUpdatedDate(LocalDateTime.now());

            m = em.merge(m);

            log.info("update mountain success {}", m);
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
        LoggerUtils.logTime(log, Thread.currentThread().
                getStackTrace()[1].
                getMethodName(), start, end);
        return ResponseEntity.status(status).
                body(rm);
    }

    @GetMapping("/list-mountain/{id}")
    public HttpEntity<ResponseEnvelope> listMountain(@PathVariable String id) {
        ResponseEnvelope rm = new ResponseEnvelope(ErrCode.SUCCESS.getCode(), ErrCode.SUCCESS.getMessage());

        Map<String, Object> response = mountainService.listMountain(id);

        HttpStatus status = HttpStatus.OK;
        rm.setData(response);
        return ResponseEntity.status(status).body(rm);
    }

    @PostMapping("/input-basecamp/{id}")
    public HttpEntity<ResponseEnvelope> inputBasecamp(BasecampForm form, @PathVariable String id, @RequestParam("photo") MultipartFile photo) {
        long start = System.currentTimeMillis();
        HttpStatus status = HttpStatus.OK;
        ErrCode errCode = ErrCode.SUCCESS;
        ResponseEnvelope rm = new ResponseEnvelope(errCode.getCode(), "Success");

        EntityManager em = userDao.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {

            String mountainId = form.getMountainId();
            String basecampName = form.getBasecampName();
            String description = form.getDescription();
            String photoNew = photo.getOriginalFilename();
            String regulation = form.getRegulation();
            String fullAddress = form.getFullAddress();
            Double price = form.getPrice();

            User u = userDao.findUser(id);

            if (u == null) {
                throw new NonexistentEntityException(ErrCode.NOT_ACCEPTABLE, "User not listed");
            }

            String role = u.getRole().toString();

            if (!role.equals("SYSADMIN")) {
                throw new NonexistentEntityException(ErrCode.NOT_ACCEPTABLE, "User not accept");
            }

            Mountain m = mountainDao.findMountain(mountainId);

            if (m == null) {
                throw new NonexistentEntityException(ErrCode.NOT_ACCEPTABLE, "Mountain not listed");
            }

            Basecamp b = new Basecamp(basecampName, description, regulation, fullAddress, price);
            b.setCreatedBy(u.getUsername());
            b.setMountainId(mountainId);
            b.setPhoto(photoNew);

            b = basecampRepo.save(b);

            Path folderBasecampPath = Paths.get(AppConstant.BASE_FOLDER_PATH, AppConstant.FOLDER_MOUNTAIN_PATH
                    + "/" + mountainId, "/basecamp/" + basecampName);
            File folderBasecamp = folderBasecampPath.toFile();
            if (!folderBasecamp.exists() || !folderBasecamp.isDirectory()) {
                folderBasecamp.mkdir();
                logger.info("User folder didn't exist, create one at {}.", folderBasecamp.getAbsolutePath());
            }
            basecampDao.saveDocBaseCamp(folderBasecampPath.toString(), b, photo);

            log.info("input basecamp success {}", b);
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
        LoggerUtils.logTime(log, Thread.currentThread().
                getStackTrace()[1].
                getMethodName(), start, end);
        return ResponseEntity.status(status).
                body(rm);
    }

    @PutMapping("/update-basecamp/{id}")
    public HttpEntity<ResponseEnvelope> updateBasecamp(@RequestBody BasecampForm form, @PathVariable String id) {
        long start = System.currentTimeMillis();
        HttpStatus status = HttpStatus.OK;
        ErrCode errCode = ErrCode.SUCCESS;
        ResponseEnvelope rm = new ResponseEnvelope(errCode.getCode(), "Success");

        EntityManager em = basecampDao.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {

            String basecampId = form.getBasecampId();
            String basecampName = form.getBasecampName();
            String description = form.getDescription();
            String regulation = form.getRegulation();
            String fullAddress = form.getFullAddress();
            Double price = form.getPrice();

            User u = userDao.findUser(id);

            if (u == null) {
                throw new NonexistentEntityException(ErrCode.NOT_ACCEPTABLE, "User not listed");
            }

            String role = u.getRole().toString();

            if (role.equals("USER")) {
                throw new NonexistentEntityException(ErrCode.NOT_ACCEPTABLE, "User not accept");
            }

            Basecamp b = basecampDao.findBasecamp(basecampId);

            b.setBasecampName(basecampName);
            b.setDescription(description);
            b.setRegulation(regulation);
            b.setFullAddress(fullAddress);
            b.setPrice(price);
            b.setUpdatedBy(u.getUsername());
            b.setUpdatedDate(LocalDateTime.now());

            b = em.merge(b);

            log.info("success update basecamp {}", b);
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
        LoggerUtils.logTime(log, Thread.currentThread().
                getStackTrace()[1].
                getMethodName(), start, end);
        return ResponseEntity.status(status).
                body(rm);
    }

    @GetMapping("/list-basecamp/{mountainId}")
    public HttpEntity<ResponseEnvelope> listBascamp(@PathVariable String mountainId) {
        ResponseEnvelope rm = new ResponseEnvelope(ErrCode.SUCCESS.getCode(), ErrCode.SUCCESS.getMessage());

        Map<String, Object> response = mountainService.listBasecamp(mountainId);

        HttpStatus status = HttpStatus.OK;
        rm.setData(response);
        return ResponseEntity.status(status).body(rm);
    }

    @PostMapping("/input-ranger/{id}")
    public HttpEntity<ResponseEnvelope> inputRangers(@RequestBody UserForm form, @PathVariable String id){
        long start = System.currentTimeMillis();
        HttpStatus status = HttpStatus.OK;
        ErrCode errCode = ErrCode.SUCCESS;
        ResponseEnvelope rm = new ResponseEnvelope(errCode.getCode(), "Success");

        EntityManager em = userDao.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            String mountainId = form.getMountainId();
            String nik = form.getNik();
            String phoneNumber = form.getPhoneNumber();
            String username = form.getUsername();
            String firstName = form.getFirstName();
            String lastName = form.getLastName();
            String address = form.getAddress();
            String email = form.getEmail();
            String pin = form.getPin();
            String confirmationPin = form.getConfirmationPin();
            String role = form.getRole();

            User adm = userDao.findUser(id);

            String roleAdm = adm.getRole().toString();

            if (!roleAdm.equals("SYSADMIN")) {
                throw new NonexistentEntityException(ErrCode.NOT_ACCEPTABLE, "User not accept");
            }

            ValidationUtils.rejectIfEmptyField(" cannot be empty",
                    new String[][]{{"ID Card", nik}, {"Phone Number", phoneNumber}, {"Username", username}, {"First Name", firstName},
                            {"Last Name", lastName}, {"Email", email}, {"Full Address", address}, {"Pin", pin},
                            {"Confirmation Pin", confirmationPin}, {"role", role}});

            ValidationUtils.rejectIfWhitespaceField(" cannot be white space",
                    new String[][]{{"Email", email}, {"Phone Number", phoneNumber}, {"Pin", pin}, {"Confirmation Pin", confirmationPin},
                            {"Username", username}, {"role", role}});

            ValidationUtils.validateTipeUser(role);
            Role.ERole typeUser = Role.ERole.valueOf(role.toUpperCase());

            String fieldName = typeUser == Role.ERole.RANGER ? "User Name" : "Name";

            ValidationUtils.rejectIfNotAlphanumeric(" Just number and letters",
                    new String[][]{{fieldName, firstName}, {fieldName, lastName}});

            ValidationUtils.validateEmail(email);
            ValidationUtils.validateNumber(phoneNumber, "Phone number format invalid");

            if (!pin.equals(confirmationPin)) {
                throw new InvalidFieldException(ErrCode.NOT_ACCEPTABLE, "Combination pin invalid");
            }

            User u = userRepo.findByPhoneNumberAndRole(phoneNumber, typeUser);

            if (u != null) {
                throw new InvalidFieldException(ErrCode.IM_USED, "User already registered");
            }

            if (StringUtils.isNotEmpty(email)) {
                User emailCheck = userRepo.findByEmailAndRole(email, typeUser);
                if (emailCheck != null) {
                    throw new PreexistingUserException(ErrCode.IM_USED, "Email already registered");
                }
            }

            User phone = userRepo.findByPhoneNumber(phoneNumber);

            if (phone != null) {
                throw new InvalidFieldException(ErrCode.IM_USED, "Phone number already registered");
            }

            Set<Role> roles = new HashSet<>();
            Role r = roleRepo.findByName(typeUser);
            roles.add(r);

            String encryptPassword = CodecUtils.encodeBcrypt(pin);

            Mountain mountain = mountainRepo.findByMountainName(adm.getFirstName());

            User user = new User(nik, phoneNumber, username, firstName, lastName,
                    address, email, encryptPassword, encryptPassword, typeUser, false);

            if (mountainId.equals("")) {
                user.setMountainId(mountainId);
            } else {
                user.setMountainId(mountain.getId());
            }
            user.setRoles(roles);
            userRepo.save(user);

            log.info("registered user success {}", user);
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
        LoggerUtils.logTime(log, Thread.currentThread().
                getStackTrace()[1].
                getMethodName(), start, end);
        return ResponseEntity.status(status).
                body(rm);
    }

    @PutMapping("/delete-ranger/{id}")
    public HttpEntity<ResponseEnvelope> deleteRanger(@RequestBody Map<String, Object> data, @PathVariable String id) {
        long start = System.currentTimeMillis();
        HttpStatus status = HttpStatus.OK;
        ErrCode errCode = ErrCode.SUCCESS;
        ResponseEnvelope rm = new ResponseEnvelope(errCode.getCode(), "Success");

        EntityManager em = userDao.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            String rangerId = (String) data.get("rangerId");

            User u = userDao.findUser(id);

            if (u == null) {
                throw new NonexistentEntityException(ErrCode.NOT_ACCEPTABLE, "User not listed");
            }

            u.setIsDeleted(true);
            u = em.merge(u);

            log.info("success delete ranger {}", u);
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
        LoggerUtils.logTime(log, Thread.currentThread().
                getStackTrace()[1].
                getMethodName(), start, end);
        return ResponseEntity.status(status).
                body(rm);
    }

    @GetMapping("/list-ranger/{mountainId}")
    public HttpEntity<ResponseEnvelope> listRanger(@PathVariable String mountainId) {
        ResponseEnvelope rm = new ResponseEnvelope(ErrCode.SUCCESS.getCode(), ErrCode.SUCCESS.getMessage());

        Map<String, Object> response = mountainService.listRanger(mountainId);

        HttpStatus status = HttpStatus.OK;
        rm.setData(response);
        return ResponseEntity.status(status).body(rm);
    }

    @GetMapping("/list-status-mountain/{mountainId}")
    public HttpEntity<ResponseEnvelope> listStatusMountain(@PathVariable String mountainId) {
        ResponseEnvelope rm = new ResponseEnvelope(ErrCode.SUCCESS.getCode(), ErrCode.SUCCESS.getMessage());

        Map<String, Object> response = mountainService.listStatusMountain(mountainId);

        HttpStatus status = HttpStatus.OK;
        rm.setData(response);
        return ResponseEntity.status(status).body(rm);
    }
}
