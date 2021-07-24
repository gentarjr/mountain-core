package com.mountain.controller.v1.user;

import com.mountain.dao.UserDao;
import com.mountain.domain.form.EquipmentForm;
import com.mountain.domain.form.MemberForm;
import com.mountain.domain.form.RequestMountainForm;
import com.mountain.domain.form.TransactionForm;
import com.mountain.entity.detail.*;
import com.mountain.entity.user.User;
import com.mountain.library.domain.ErrCode;
import com.mountain.library.domain.ResponseEnvelope;
import com.mountain.library.exceptions.NonexistentEntityException;
import com.mountain.library.exceptions.WinterfellException;
import com.mountain.library.helper.LoggerUtils;
import com.mountain.library.helper.ValidationUtils;
import com.mountain.repo.*;
import com.mountain.service.internal.MountainServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final MemberRepo memberRepo;
    private final UserRepo userRepo;
    private final AppRepo appRepo;
    private final MountainServiceClient mountainServiceClient;
    private final EquipmentRepo equipmentRepo;
    private final RequestMountainRepo requestMountainRepo;
    private final TransactionRepo transactionRepo;

    private final UserDao userDao;

    @PostMapping("/{id}/input-climber")
    public HttpEntity<ResponseEnvelope> inputClimber(@PathVariable String id, @RequestBody RequestMountainForm form) {

        long start = System.currentTimeMillis();
        HttpStatus status = HttpStatus.OK;
        ErrCode errCode = ErrCode.SUCCESS;
        ResponseEnvelope rm = new ResponseEnvelope(errCode.getCode(), "Sukses");

        EntityManager em = userDao.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        Map<String, Object> result;
        try {
            Integer totalClimber = form.getTotalClimber();
            String deviceId = form.getDeviceId();
            String request = form.getRequest();
            String reason = "I want to climb";
            String statusAccount = "REQUEST";
            List<MemberForm> memberForm = form.getMember();
            EquipmentForm equipmentForm = form.getEquipment();
            TransactionForm transactionForm = form.getTransaction();

            User u = userDao.findUser(id);

            if (u == null) {
                throw new NonexistentEntityException(ErrCode.NO_CONTENT, "User not listed");
            }

            Integer backPack = equipmentForm.getBackPack();
            Integer water = equipmentForm.getWater();
            Integer mattress = equipmentForm.getMattress();
            Integer tent = equipmentForm.getTent();
            Integer food = equipmentForm.getFood();
            Integer stove = equipmentForm.getStove();
            Integer nesting = equipmentForm.getNesting();
            Integer rainCoat = equipmentForm.getRainCoat();
            Integer flashLight = equipmentForm.getFlashLight();

            String mountain = transactionForm.getMountain();
            Double payment = transactionForm.getPayment();
            String stats = transactionForm.getStatus();
            String reas = transactionForm.getReason();
            String date = transactionForm.getDate();

            RequestMountain reqm = new RequestMountain(totalClimber, request, statusAccount, reason, deviceId);
            Equipment equipment = new Equipment(backPack, water, mattress, tent, food,
                    stove, nesting, rainCoat, flashLight);
            Transaction tr = new Transaction(mountain, payment, stats, reas, date);
            reqm.setUser(u);
            reqm.setEquipment(equipment);
            reqm.setTransaction(tr);
            equipment = equipmentRepo.save(equipment);
            tr = transactionRepo.save(tr);
            requestMountainRepo.save(reqm);

            for (MemberForm m : memberForm) {
                // field cannot be empty
                String idCard = m.getIdCard();
                String name = m.getName();
                String phoneNumber = m.getPhoneNumber();
                String provinsi = m.getProvinsi();
                String kota = m.getKota();
                String kecamatan = m.getKecamatan();
                String rtrw = m.getRtRw();

                ValidationUtils.rejectIfEmptyField(" cannot be empty",
                        new String[][]{{"ID Card", idCard}, {"Name", name},
                                {"Phone Number", phoneNumber}, {"Provinsi", provinsi}, {"Kota", kota},
                                {"Kecamatan", kecamatan}, {"RT/RW", rtrw}});

                Member member = new Member(idCard, name, phoneNumber, provinsi,
                        kota, kecamatan, rtrw);
                member.setRequestMountain(reqm);
                memberRepo.save(member);
            }

            App.AppCode nameApp = App.AppCode.MOUNTAIN;
            App app = appRepo.findByName(nameApp);

            result = mountainServiceClient.notificationRegisterClimber(new RequestMountainForm(u.getFirstName(), stats, reas, deviceId, app.getAppId(), app.getAppKey()));

            log.info("User {} success Input climber", u.getId());
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
}
