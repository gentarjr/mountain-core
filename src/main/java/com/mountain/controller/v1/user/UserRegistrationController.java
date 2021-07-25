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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping(value = "/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserRegistrationController {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;

    @PostMapping("/register/pre-check")
    public HttpEntity<ResponseEnvelope> preRegisterCheck(@RequestBody UserForm form){
        long start = System.currentTimeMillis();
        HttpStatus status = HttpStatus.OK;
        ErrCode errCode = ErrCode.SUCCESS;
        ResponseEnvelope rm = new ResponseEnvelope(errCode.getCode(), "Success");
        try{

            String phoneNumber = form.getPhoneNumber();
            String username = form.getUsername();
            String email = form.getEmail();
            String nik = form.getNik();
            String role = form.getRole();

            ValidationUtils.rejectIfEmptyField(" Cannot be empty",
                    new String[][]{{"Phone Number", phoneNumber}, {"NIK", nik}, {"Email", email}, {"Role user", role}});

            ValidationUtils.validateTipeUser(role);
            ERole typeUser = ERole.valueOf(role.toUpperCase());

            ValidationUtils.validateEmail(email);
            ValidationUtils.validateNumber(phoneNumber, "Phone number format invalid");

            User u = userRepo.findByPhoneNumberOrUsernameOrEmailOrNikAndRole(phoneNumber, username, email, nik, typeUser);

            if(u != null){
                throw new PreexistingUserException(ErrCode.IM_USED, "Phone number or email or idCard already registered");
            }

            log.info("Phone number {}, email {}, role {} can not be registered", phoneNumber, email, typeUser);
        }catch (WinterfellException e) {
            status = HttpStatus.CONFLICT;
            rm.setCode(e.getErrCode().getCode());
            rm.setMessage(e.getMessage());
            log.warn("Exception Caught :", e);
        } catch (Exception e) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            rm.setCode(ErrCode.BAD_REQUEST.getCode());
            rm.setMessage(ErrCode.BAD_REQUEST.getMessage());

            log.warn("Exception Caught :", e);
        }
        long end = System.currentTimeMillis();
        LoggerUtils.logTime(log, Thread.currentThread().getStackTrace()[1].getMethodName(), start, end);
        return ResponseEntity.status(status).body(rm);
    }

    @PostMapping("/register")
    public HttpEntity<ResponseEnvelope> registerUser(@RequestBody UserForm form){
        long start = System.currentTimeMillis();
        HttpStatus status = HttpStatus.OK;
        ErrCode errCode = ErrCode.SUCCESS;
        ResponseEnvelope rm = new ResponseEnvelope(errCode.getCode(), "Success");

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

            ValidationUtils.rejectIfEmptyField(" cannot be empty",
                    new String[][]{{"ID Card", nik}, {"Phone Number", phoneNumber}, {"Username", username}, {"First Name", firstName},
                            {"Last Name", lastName}, {"Email", email}, {"Full Address", address}, {"Pin", pin},
                            {"Confirmation Pin", confirmationPin}, {"role", role}});

            ValidationUtils.rejectIfWhitespaceField(" cannot be white space",
                    new String[][]{{"Email", email}, {"Phone Number", phoneNumber}, {"Pin", pin}, {"Confirmation Pin", confirmationPin},
                            {"Username", username}, {"role", role}});

            ValidationUtils.validateTipeUser(role);
            ERole typeUser = ERole.valueOf(role.toUpperCase());

            String fieldName = typeUser == ERole.USER ? "User Name" : "Name";

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

            User phone = userRepo.findByPhoneNumber(phoneNumber);

            if(phone != null){
                throw new InvalidFieldException(ErrCode.IM_USED, "Phone number already registered");
            }

            if (StringUtils.isNotEmpty(email)) {
                User emailCheck = userRepo.findByEmailAndRole(email, typeUser);
                if(emailCheck != null){
                    throw new PreexistingUserException(ErrCode.IM_USED, "Email already registered");
                }
            }

            Set<Role> roles = new HashSet<>();
            Role r = roleRepo.findByName(typeUser);
            roles.add(r);

            String encryptPassword = CodecUtils.encodeBcrypt(pin);

            Timestamp createdDate = new Timestamp(System.currentTimeMillis());

            User user = new User(nik, phoneNumber, username, firstName, lastName,
                    address, email, encryptPassword, encryptPassword, typeUser, createdDate);

            user.setRoles(roles);
            userRepo.save(user);

            log.info("registered user success {}", user);
        }catch (WinterfellException e) {
            status = HttpStatus.CONFLICT;
            rm.setCode(e.getErrCode().getCode());
            rm.setMessage(e.getMessage());
            log.warn("Exception Caught :", e);
        } catch (Exception e) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            rm.setCode(ErrCode.BAD_REQUEST.getCode());
            rm.setMessage(ErrCode.BAD_REQUEST.getMessage());

            log.warn("Exception Caught :", e);
        }
        long end = System.currentTimeMillis();
        LoggerUtils.logTime(log, Thread.currentThread().getStackTrace()[1].getMethodName(), start, end);
        return ResponseEntity.status(status).body(rm);
    }
}
