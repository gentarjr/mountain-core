package com.mountain.controller.v1.user;

import com.mountain.domain.form.UserForm;
import com.mountain.entity.role.Role;
import com.mountain.entity.role.Role.ERole;
import com.mountain.entity.user.User;
import com.mountain.library.domain.ErrCode;
import com.mountain.library.domain.ResponseEnvelope;
import com.mountain.library.exceptions.InvalidFieldException;
import com.mountain.library.exceptions.PreexistingUserException;
import com.mountain.library.exceptions.WinterfellException;
import com.mountain.library.helper.CodecUtils;
import com.mountain.library.helper.LoggerUtils;
import com.mountain.library.helper.ValidationUtils;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping(value = "/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserRegistrationController {

    private static final Logger logger = LoggerFactory.getLogger(UserRegistrationController.class);

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;

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
            ERole roleUser = ERole.valueOf(role.toUpperCase());

            ValidationUtils.validateEmail(email);
            ValidationUtils.validateNumber(phoneNumber, "Phone number format invalid");

            userRepo.findByPhoneNumberOrEmailOrIdCardAndRole(phoneNumber, email, idCard, roleUser).ifPresent(p -> {
                throw new PreexistingUserException(ErrCode.INF_FIELDINVALID, "Phone number or email or idCard already registered");
            });

            log.info("Phone number {}, email {}, type {} can not be registered", phoneNumber, email, roleUser);
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

    @PostMapping("/register")
    public HttpEntity<ResponseEnvelope> registerUser(@RequestBody UserForm form) {
        long start = System.currentTimeMillis();
        HttpStatus status = HttpStatus.OK;
        ErrCode errCode = ErrCode.SUCCESS;
        ResponseEnvelope rm = new ResponseEnvelope(errCode.getCode(), "Sukses");
        try {

            String idCard = form.getIdCard();
            String firstName = form.getFirstName();
            String lastName = form.getLastName();
            String email = form.getEmail();
            String phoneNumber = form.getPhoneNumber();
            String fullAddress = form.getFullAddress();
            String pin = form.getPin();
            String confirmationPin = form.getConfirmationPin();
            String bankName = form.getBankName();
            String accountName = form.getAccountName();
            String accountNumber = form.getAccountNumber();
            String role = form.getRole();


            // field cannot be empty
            ValidationUtils.rejectIfEmptyField(" cannot be empty",
                    new String[][]{{"ID Card", idCard}, {"First Name", firstName}, {"Last Name", lastName}, {"Email", email},
                            {"Phone Number", phoneNumber}, {"Full Address", fullAddress}, {"Pin", pin},
                            {"Confirmation Pin", confirmationPin}, {"Bank Name", bankName}, {"Account Name", accountName},
                            {"Account Number", accountNumber}, {"role", role}});

            //field cannot be with whitespace
            ValidationUtils.rejectIfWhitespaceField(" cannot be empty",
                    new String[][]{{"Email", email}, {"Phone Number", phoneNumber}, {"Pin", pin}, {"Confirmation Pin", confirmationPin},
                            {"Account Number", accountNumber}, {"role", role}});

            ValidationUtils.validateTipeUser(role);
            ERole roleUser = ERole.valueOf(role.toUpperCase());

            String namaFieldName = roleUser == ERole.USER ? "User Name" : "Nama";

            //just number and letters
            ValidationUtils.rejectIfNotAlphanumeric(" Just number and letters",
                    new String[][]{{namaFieldName, firstName},{namaFieldName, lastName}});

            ValidationUtils.validateEmail(email);
            ValidationUtils.validateNumber(phoneNumber, "Phone number format invalid");

            if (!pin.equals(confirmationPin)) {
                throw new InvalidFieldException(ErrCode.INF_FIELDINVALID, "Combination pin invalid");
            }

            User u = userRepo.findByPhoneNumberAndRole(phoneNumber, roleUser);
            if(u != null){
                throw new PreexistingUserException(ErrCode.INF_USERNOTEMPTY, "Phone number already registered");
            }

            if (StringUtils.isNotEmpty(email)) {
                userRepo.findByEmailAndRole(email, roleUser).ifPresent(p -> {
                    throw new PreexistingUserException(ErrCode.INF_USERNOTEMPTY, "Email already registered");
                });
            }

            Set<Role> roles = new HashSet<>();
            Role r = roleRepo.findByName(ERole.USER);
            roles.add(r);

            String encryptPassword = CodecUtils.encodeBcrypt(pin);

            User user = new User(idCard, phoneNumber, firstName, lastName, fullAddress, email, bankName,
                    accountName, accountNumber, encryptPassword, encryptPassword, roleUser);

            user.setRoles(roles);
            userRepo.save(user);

            log.info("registered user success {}", user);
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
