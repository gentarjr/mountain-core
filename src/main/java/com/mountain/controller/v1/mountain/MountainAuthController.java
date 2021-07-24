package com.mountain.controller.v1.mountain;

import com.mountain.dao.UserDao;
import com.mountain.domain.form.UserForm;
import com.mountain.domain.response.UserResponse;
import com.mountain.entity.role.Role;
import com.mountain.entity.user.User;
import com.mountain.library.domain.ErrCode;
import com.mountain.library.domain.ResponseEnvelope;
import com.mountain.library.exceptions.ForbiddenOperationException;
import com.mountain.library.exceptions.NonexistentEntityException;
import com.mountain.library.exceptions.PreexistingUserException;
import com.mountain.library.exceptions.WinterfellException;
import com.mountain.library.helper.CodecUtils;
import com.mountain.library.helper.LoggerUtils;
import com.mountain.library.helper.ValidationUtils;
import com.mountain.repo.UserRepo;
import com.mountain.spring.principal.PrincipalRepo;
import com.mountain.spring.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "/v1/mountain/auth")
@Slf4j
@RequiredArgsConstructor
public class MountainAuthController {

    private final UserDao userDao;

    private final UserRepo userRepo;
    private final PrincipalRepo principalRepo;

    private final JwtService jwtService;

    @PostMapping("/check-login")
    public HttpEntity<ResponseEnvelope> preLoginCheck(@RequestBody UserForm form) {
        long start = System.currentTimeMillis();
        HttpStatus status = HttpStatus.OK;
        ErrCode errCode = ErrCode.SUCCESS;

        ResponseEnvelope rm = new ResponseEnvelope(errCode.getCode(), "Sukses");
        try {
            String phoneNumber = form.getPhoneNumber();
            String role = form.getRole();

            ValidationUtils.rejectIfEmptyField(" Cannot be empty",
                    new String[][]{{"Phone number", phoneNumber}, {"Type user", role}});

            ValidationUtils.validateTipeUser(role);
            Role.ERole tipeUser = Role.ERole.valueOf(role.toUpperCase());

            ValidationUtils.validateNumber(phoneNumber, "Phone number format invalid");


            User u = userRepo.findByPhoneNumberAndRole(phoneNumber, tipeUser);
            if(u != null){
                throw new PreexistingUserException(ErrCode.INF_USERNOTEMPTY, "Phone number already registered");
            }

            log.info("chek login success {}", phoneNumber);
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

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpEntity<ResponseEnvelope> login(HttpServletRequest request, HttpServletResponse response,
                                              @RequestParam(defaultValue = "") String phoneNumber, @RequestParam(defaultValue = "") String password,
                                              @RequestParam(defaultValue = "") String roles, @RequestParam(defaultValue = "") String deviceId) {

        HttpStatus status = HttpStatus.OK;
        ErrCode errCode = ErrCode.SUCCESS;
        ResponseEnvelope rm = new ResponseEnvelope(errCode.getCode(), "Sukses");

        EntityManager em = userDao.getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {

            String role = roles;

            User u = principalRepo.findByPhoneNumberAndRoleName(phoneNumber, role);

            if (u == null) {
                throw new NonexistentEntityException(ErrCode.INF_USEREMPTY, "User not listed");
            }

            ValidationUtils.rejectIfEmptyField(" Cannot be empty",
                    new String[][]{{"Phone number", phoneNumber}, {"PIN", password}});

            if (!CodecUtils.isPasswordMatch(password, u.getPin())) {
                throw new ForbiddenOperationException(ErrCode.ERR_BADCRADENTIAL, "PIN you entered is wrong");
            }

            UserResponse userResponse;
            String token = jwtService.generateJwtToken(u);

            userResponse = new UserResponse(token, u.getId(), u.getIdCard(), u.getFirstName(), u.getLastName(), u.getMountainName(), u.getPhoneNumber(),
                    u.getEmail(), u.getAddress(), u.getRoleName(), u.getCreatedDate(), u.getUpdatedDate());

            rm.putData("user", userResponse);
            log.info("login User sukses {}", u.getPhoneNumber());
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
            rm.setCode(ErrCode.ERR_UNKNOWN.getCode());
            rm.setMessage(ErrCode.ERR_UNKNOWN.getMessage());
            log.warn("Exception Caught :", e);
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }

        return ResponseEntity.status(status).body(rm);
    }
}
