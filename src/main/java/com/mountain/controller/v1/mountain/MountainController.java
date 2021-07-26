package com.mountain.controller.v1.mountain;

import com.mountain.dao.MountainDao;
import com.mountain.dao.UserDao;
import com.mountain.domain.form.MountainForm;
import com.mountain.domain.form.UserForm;
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
import com.mountain.repo.MountainRepo;
import com.mountain.repo.RoleRepo;
import com.mountain.repo.UserRepo;
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
import java.sql.Timestamp;
import java.util.HashSet;
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
    private final MountainDao mountainDao;

    @PostMapping("/input-mountain/{id}")
    public HttpEntity<ResponseEnvelope> inputMountain(MountainForm form , @PathVariable String id, @RequestParam("photo")MultipartFile photo){

        long start = System.currentTimeMillis();
        HttpStatus status = HttpStatus.OK;
        ErrCode errCode = ErrCode.SUCCESS;
        ResponseEnvelope rm = new ResponseEnvelope(errCode.getCode(), "Success");

        EntityManager em = userDao.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            User u = userDao.findUser(id);

            String mountainName = form.getMountainName();
            String basecampName = form.getBasecampName();
            String description = form.getDescription();
            Integer height = form.getHeight();
            String regulation = form.getRegulation();
            String fullAddress = form.getFullAddress();
            Double price = form.getPrice();

            String photoNew = photo.getOriginalFilename();

            if(u == null){
                throw new NonexistentEntityException(ErrCode.NOT_ACCEPTABLE, "User not listed");
            }

            String role = u.getRole().toString();
            if(!role.equals("SYSADMIN")){
                throw new NonexistentEntityException(ErrCode.NOT_ACCEPTABLE, "User not accept");
            }

            if(!mountainName.equals(u.getFirstName())){
                throw new NonexistentEntityException(ErrCode.NOT_ACCEPTABLE, "Mountain name is wrong");
            }

            if(!basecampName.equals(u.getLastName())){
                throw new NonexistentEntityException(ErrCode.NOT_ACCEPTABLE, "Basecamp name is wrong");
            }

            ValidationUtils.rejectIfEmptyField(" cannot be empty",
                    new String[][]{{"Mountain name", mountainName}, {"Basecamp Name", basecampName},
                            {"Description", description}, {"Regulation", regulation}, {"Full Address", fullAddress}});

            Timestamp createdDate = new Timestamp(System.currentTimeMillis());

            Mountain m = mountainRepo.findByMountainNameAndBasecampName(mountainName, basecampName);

            if(m != null){
                throw new NonexistentEntityException(ErrCode.NOT_ACCEPTABLE, "Mountain and Basecamp already registered");
            }



            Mountain mountain = new Mountain(mountainName, basecampName, description, height, regulation, fullAddress, price, createdDate, u.getUsername());

            if(!photoNew.equals("")){
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
        }catch (WinterfellException e) {
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

    @PostMapping("/input-ranger/{id}")
    public HttpEntity<ResponseEnvelope> inputRangers(@RequestBody UserForm form, @PathVariable String id){

        long start = System.currentTimeMillis();
        HttpStatus status = HttpStatus.OK;
        ErrCode errCode = ErrCode.SUCCESS;
        ResponseEnvelope rm = new ResponseEnvelope(errCode.getCode(), "Success");

        EntityManager em = userDao.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try{
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

            if(!roleAdm.equals("SYSADMIN")){
                throw new NonexistentEntityException(ErrCode.NOT_ACCEPTABLE, "User not accept");
            }

            if(adm.getMountainId().equals("")){
                throw new NonexistentEntityException(ErrCode.NO_CONTENT, "Please input mountain first");
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
                    new String[][]{{fieldName, firstName},{fieldName, lastName}});

            ValidationUtils.validateEmail(email);
            ValidationUtils.validateNumber(phoneNumber, "Phone number format invalid");

            if (!pin.equals(confirmationPin)) {
                throw new InvalidFieldException(ErrCode.NOT_ACCEPTABLE, "Combination pin invalid");
            }

            User u = userRepo.findByPhoneNumberAndRole(phoneNumber, typeUser);

            if(u != null){
                throw new InvalidFieldException(ErrCode.IM_USED, "User already registered");
            }

            if (StringUtils.isNotEmpty(email)) {
                User emailCheck = userRepo.findByEmailAndRole(email, typeUser);
                if(emailCheck != null){
                    throw new PreexistingUserException(ErrCode.IM_USED, "Email already registered");
                }
            }

            User phone = userRepo.findByPhoneNumber(phoneNumber);

            if(phone != null){
                throw new InvalidFieldException(ErrCode.IM_USED, "Phone number already registered");
            }

            Set<Role> roles = new HashSet<>();
            Role r = roleRepo.findByName(typeUser);
            roles.add(r);

            String encryptPassword = CodecUtils.encodeBcrypt(pin);

            Timestamp createdDate = new Timestamp(System.currentTimeMillis());

            Mountain mountain = mountainRepo.findByMountainNameAndBasecampName(adm.getFirstName(), adm.getLastName());

            User user = new User(nik, phoneNumber, username, firstName, lastName,
                    address, email, encryptPassword, encryptPassword, typeUser, createdDate);

            user.setMountainId(mountain.getId());
            user.setRoles(roles);
            userRepo.save(user);

            log.info("registered user success {}", user);
        }catch (WinterfellException e) {
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
}
