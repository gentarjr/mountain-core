package com.mountain.service.mountain.impl;


import com.mountain.dao.MountainDao;
import com.mountain.dao.UserDao;
import com.mountain.domain.response.BasecampResponse;
import com.mountain.domain.response.MountainResponse;
import com.mountain.domain.response.StatusResponse;
import com.mountain.domain.response.UserResponse;
import com.mountain.entity.detail.Basecamp;
import com.mountain.entity.detail.Mountain;
import com.mountain.entity.role.Role.ERole;
import com.mountain.entity.user.Status;
import com.mountain.entity.user.User;
import com.mountain.library.domain.ErrCode;
import com.mountain.library.exceptions.NonexistentEntityException;
import com.mountain.repo.BasecampRepo;
import com.mountain.repo.MountainRepo;
import com.mountain.repo.StatusRepo;
import com.mountain.repo.UserRepo;
import com.mountain.service.ServiceDataList;
import com.mountain.service.mountain.MountainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MountainServiceImpl implements MountainService {

    private final MountainRepo mountainRepo;
    private final BasecampRepo basecampRepo;
    private final UserRepo userRepo;
    private final StatusRepo statusRepo;

    private final UserDao userDao;
    private final MountainDao mountainDao;

    private final ServiceDataList serviceDataList;

    @Override
    public Map<String, Object> listMountain(String id) {

        Map<String, Object> result = new HashMap<>();

        User u = userDao.findUser(id);

        if(u == null){
            throw new NonexistentEntityException(ErrCode.NOT_ACCEPTABLE, "User not listed");
        }

        List<Mountain> m = mountainRepo.findAll();

        List<MountainResponse> listMountainResponse = serviceDataList.listMountain(m);

        result.put("mountain", listMountainResponse);

        return result;
    }

    @Override
    public Map<String, Object> listBasecamp(String mountainId) {
        Map<String, Object> result = new HashMap<>();

        Mountain m = mountainDao.findMountain(mountainId);

        if(m == null){
            throw new NonexistentEntityException(ErrCode.NOT_ACCEPTABLE, "Mountain not listed");
        }

        List<Basecamp> b = basecampRepo.findAll();

        List<BasecampResponse> listBasecampResponse = serviceDataList.listBasecamp(b);

        result.put("basecamp", listBasecampResponse);

        return result;
    }

    @Override
    public Map<String, Object> listRanger(String mountainId) {
        Map<String, Object> result = new HashMap<>();
        ERole role = ERole.RANGER;

        Mountain mountain = mountainDao.findMountain(mountainId);

        if(mountain == null){
            throw new NonexistentEntityException(ErrCode.NOT_ACCEPTABLE, "Ranger not listed");
        }

        List<User> u = userRepo.findByMountainIdAndRoleOrderByCreatedDateAsc(mountainId, role);

        List<UserResponse> listUserResponse = serviceDataList.listRanger(u);

        result.put("ranger", listUserResponse);
        return result;
    }

    @Override
    public Map<String, Object> listStatusMountain(String mountainId) {
        Map<String, Object> result = new HashMap<>();

        Mountain m = mountainDao.findMountain(mountainId);

        if(m == null){
            throw new NonexistentEntityException(ErrCode.NOT_ACCEPTABLE, "Mountain not listed");
        }

        List<Status> status = statusRepo.findByMountainIdOrderByCreatedDateAsc(mountainId);

        List<StatusResponse> listStatusResponse = serviceDataList.listStatusMountain(status);

        result.put("status", listStatusResponse);

        return result;
    }
}
