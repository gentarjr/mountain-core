package com.mountain.service.user.impl;

import com.mountain.dao.StatusDao;
import com.mountain.dao.UserDao;
import com.mountain.domain.response.StatusResponse;
import com.mountain.entity.user.Status;
import com.mountain.entity.user.User;
import com.mountain.library.domain.ErrCode;
import com.mountain.library.exceptions.NonexistentEntityException;
import com.mountain.repo.StatusRepo;
import com.mountain.repo.UserRepo;
import com.mountain.service.ServiceDataList;
import com.mountain.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final StatusDao statusDao;

    private final ServiceDataList serviceDataList;

    private final UserRepo userRepo;
    private final StatusRepo statusRepo;


    @Override
    public Map<String, Object> listStatus(String id) {
        Map<String, Object> result = new HashMap<>();

        User u = userDao.findUser(id);

        if (u == null) {
            throw new NonexistentEntityException(ErrCode.NOT_ACCEPTABLE, "User not listed");
        }

        List<Status> status = statusRepo.findStatusUser();

        List<StatusResponse> listResponseStatus = serviceDataList.listStatus(status);

        result.put("status", listResponseStatus);

        return result;
    }

    @Override
    public Map<String, Object> listStatusUser(String id) {
        Map<String, Object> result = new HashMap<>();
        User u = userDao.findUser(id);

        if (u == null) {
            throw new NonexistentEntityException(ErrCode.NOT_ACCEPTABLE, "User not listed");
        }

        List<Status> status = statusRepo.findByUsersId(id);

        List<StatusResponse> listResponseStatus = serviceDataList.listStatus(status);

        result.put("status", listResponseStatus);

        return result;
    }

    @Override
    public Map<String, Object> listReplyStatus(String statusId) {
        Map<String, Object> result = new HashMap<>();

        Status s = statusDao.findStatus(statusId);

        if (s == null) {
            throw new NonexistentEntityException(ErrCode.NOT_ACCEPTABLE, "Status not listed");
        }

        StatusResponse statusResponse = new StatusResponse();

        statusResponse = serviceDataList.listReplyStatus(s);

        result.put("data", statusResponse);

        return result;
    }
}
