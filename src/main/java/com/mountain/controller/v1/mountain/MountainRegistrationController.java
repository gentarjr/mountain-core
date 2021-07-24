package com.mountain.controller.v1.mountain;

import com.mountain.domain.form.UserForm;
import com.mountain.entity.role.Role;
import com.mountain.entity.role.RoleMountain;
import com.mountain.entity.user.User;
import com.mountain.library.domain.ErrCode;
import com.mountain.library.domain.ResponseEnvelope;
import com.mountain.library.exceptions.InvalidFieldException;
import com.mountain.library.exceptions.PreexistingUserException;
import com.mountain.library.exceptions.WinterfellException;
import com.mountain.library.helper.CodecUtils;
import com.mountain.library.helper.LoggerUtils;
import com.mountain.library.helper.ValidationUtils;
import com.mountain.repo.RoleMountainRepo;
import com.mountain.repo.RoleRepo;
import com.mountain.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/v1/mountain")
@RequiredArgsConstructor
@Slf4j
public class MountainRegistrationController {

    private final RoleRepo roleRepo;
    private final UserRepo userRepo;
    private final RoleMountainRepo roleMountainRepo;

    @PostMapping("/register/pre-check")
    public HttpEntity<ResponseEnvelope> preRegisterCheck(@RequestBody UserForm form) {
        long start = System.currentTimeMillis();
        HttpStatus status = HttpStatus.OK;
        ErrCode errCode = ErrCode.SUCCESS;

        ResponseEnvelope rm = new ResponseEnvelope(errCode.getCode(), "Sukses");
        try {

            String phoneNumber = form.getPhoneNumber();
            String email = form.getEmail();
            String idCard = form.getIdCard();
            String role = form.getRole();

            ValidationUtils.rejectIfEmptyField(" Cannot be empty",
                    new String[][]{{"Phone Number", phoneNumber}, {"Email", email}, {"Role user", role}});

            ValidationUtils.validateTipeUser(role);

            ValidationUtils.validateEmail(email);
            ValidationUtils.validateNumber(phoneNumber, "Phone number format invalid");

            userRepo.findByPhoneNumberOrEmailOrIdCardAndRoleName(phoneNumber, email, idCard,role).ifPresent(p -> {
                throw new PreexistingUserException(ErrCode.INF_FIELDINVALID, "Phone number or email or idCard already registered");
            });

            log.info("Phone number {}, email {}, type {} can not be registered", phoneNumber, email, role);
        } catch (WinterfellException e) {
            status = HttpStatus.CONFLICT;
            rm.setCode(e.getErrCode().getCode());
            rm.setMessage(e.getMessage());
            log.warn("Exception Caught :", e);
        } catch (Exception e) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            rm.setCode(ErrCode.ERR_UNKNOWN.getCode());
            rm.setMessage(ErrCode.ERR_UNKNOWN.getMessage());

            log.warn("Exception Caught :", e);
        }
        long end = System.currentTimeMillis();
        LoggerUtils.logTime(log, Thread.currentThread().getStackTrace()[1].getMethodName(), start, end);
        return ResponseEntity.status(status).body(rm);
    }

    @PostMapping(value = "/register/input-roles", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpEntity<ResponseEnvelope> inputRolesFinance(@RequestBody Map<String, Object> data) {
        long start = System.currentTimeMillis();
        HttpStatus status = HttpStatus.OK;
        ErrCode errCode = ErrCode.SUCCESS;
        ResponseEnvelope rm = new ResponseEnvelope(errCode.getCode(), "Sukses");
        try {
            String rolesName = (String) data.get("role");

            ValidationUtils.rejectIfEmptyField(" Cannot be empty", new String[][]{{"Roles", rolesName}});

            ValidationUtils.rejectIfNotAlphanumeric(" Just number and letters",
                    new String[][]{{"Roles", rolesName}});

            ValidationUtils.rejectIfWhitespaceField(" Cannot be whitespace",
                    new String[][]{{"Roles", rolesName}});


            RoleMountain r = roleMountainRepo.findByRoleName(rolesName);

            if (r != null) {
                throw new PreexistingUserException(ErrCode.INF_FIELDINVALID, "Roles has been registered");
            }
            if(rolesName.equals("USER")){
                Role.ERole tipeUser = Role.ERole.valueOf(rolesName.toUpperCase());
                Role roles = new Role(tipeUser);
                roleRepo.save(roles);
            }else{
                RoleMountain roleMountain = new RoleMountain(rolesName);
                roleMountainRepo.save(roleMountain);
            }

            log.info("Roles name {} success to input");
        } catch (WinterfellException e) {
            status = HttpStatus.CONFLICT;
            rm.setCode(e.getErrCode().getCode());
            rm.setMessage(e.getMessage());
            log.warn("Exception Caught :", e);
        } catch (Exception e) {
            status = HttpStatus.CONFLICT;
            rm.setCode(ErrCode.ERR_UNKNOWN.getCode());
            rm.setMessage(ErrCode.ERR_UNKNOWN.getMessage());

            log.warn("Exception Caught :", e);
        }

        long end = System.currentTimeMillis();
        LoggerUtils.logTime(log, Thread.currentThread().

                getStackTrace()[1].

                getMethodName(), start, end);

        return ResponseEntity.status(status).

                body(rm);

    }

    @PostMapping("/register")
    public HttpEntity<ResponseEnvelope> registerMountain(@RequestBody UserForm form) {
        long start = System.currentTimeMillis();
        HttpStatus status = HttpStatus.OK;
        ErrCode errCode = ErrCode.SUCCESS;
        ResponseEnvelope rm = new ResponseEnvelope(errCode.getCode(), "Sukses");
        try {
            String idCard = form.getIdCard();
            String phoneNumber = form.getPhoneNumber();
            String firstName = form.getFirstName();
            String lastName = form.getLastName();
            String mountainName = form.getMountainName();
            String fullAddress = form.getFullAddress();
            Double paymentClimber = form.getPaymentClimber();
            String role = form.getRole();
            String pin = form.getPin();
            String confirmationPin = form.getConfirmationPin();

            // field cannot be empty
            ValidationUtils.rejectIfEmptyField(" cannot be empty",
                    new String[][]{{"ID Card", idCard}, {"First Name", firstName}, {"Last Name", lastName}, {"Mountain Name", mountainName},
                            {"Phone Number", phoneNumber}, {"Full Address", fullAddress}, {"Pin", pin},
                            {"Confirmation Pin", confirmationPin}, {"role", role}});

            //field cannot be with whitespace
            ValidationUtils.rejectIfWhitespaceField(" cannot be empty",
                    new String[][]{{"Phone Number", phoneNumber}, {"Pin", pin}, {"Confirmation Pin", confirmationPin},
                            {"role", role}});

            ValidationUtils.validateNumber(phoneNumber, "Phone number format invalid");

            if (!pin.equals(confirmationPin)) {
                throw new InvalidFieldException(ErrCode.INF_FIELDINVALID, "Combination pin invalid");
            }

            userRepo.findByPhoneNumberAndRoleName(phoneNumber, role).ifPresent(p -> {
                throw new PreexistingUserException(ErrCode.INF_USERNOTEMPTY, "Phone number already registered");
            });

            Set<RoleMountain> roles = new HashSet<>();
            RoleMountain r = roleMountainRepo.findByRoleName(role);
            roles.add(r);

            String encryptPassword = CodecUtils.encodeBcrypt(pin);

            User user = new User(idCard, phoneNumber, firstName, lastName, mountainName, fullAddress, paymentClimber,
                    role, encryptPassword, encryptPassword);

            user.setRoleMountain(roles);
            userRepo.save(user);

            log.info("registered mountain success {}", user);
        } catch (WinterfellException e) {
            status = HttpStatus.CONFLICT;
            rm.setCode(e.getErrCode().getCode());
            rm.setMessage(e.getMessage());
            log.warn("Exception Caught :", e);
        } catch (Exception e) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            rm.setCode(ErrCode.ERR_UNKNOWN.getCode());
            rm.setMessage(ErrCode.ERR_UNKNOWN.getMessage());

            log.warn("Exception Caught :", e);
        }
        long end = System.currentTimeMillis();
        LoggerUtils.logTime(log, Thread.currentThread().getStackTrace()[1].getMethodName(), start, end);
        return ResponseEntity.status(status).body(rm);
    }
}
