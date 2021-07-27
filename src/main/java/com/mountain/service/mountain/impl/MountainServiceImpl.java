package com.mountain.service.mountain.impl;


import com.mountain.dao.UserDao;
import com.mountain.domain.response.MountainResponse;
import com.mountain.entity.detail.Mountain;
import com.mountain.entity.user.User;
import com.mountain.library.domain.ErrCode;
import com.mountain.library.exceptions.NonexistentEntityException;
import com.mountain.repo.MountainRepo;
import com.mountain.service.ServiceDataList;
import com.mountain.service.mountain.MountainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MountainServiceImpl implements MountainService {

    private final MountainRepo mountainRepo;

    private final UserDao userDao;

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
}
