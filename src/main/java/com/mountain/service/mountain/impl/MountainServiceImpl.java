package com.mountain.service.mountain.impl;

import com.mountain.domain.response.RequestMountainResponse;
import com.mountain.entity.detail.RequestMountain;
import com.mountain.entity.user.User;
import com.mountain.library.domain.ErrCode;
import com.mountain.library.exceptions.NonexistentEntityException;
import com.mountain.repo.RequestMountainRepo;
import com.mountain.repo.UserRepo;
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

    private final UserRepo userRepo;
    private final RequestMountainRepo requestMountainRepo;

    private final ServiceDataList serviceDataList;

    @Override
    public Map<String, Object> listClimber(String id) {
        Map<String, Object> result = new HashMap<>();

        User u = userRepo.findById(id);

        if(u == null){
            throw new NonexistentEntityException(ErrCode.NO_CONTENT, "User not listed");
        }

        List<RequestMountainResponse> requestMountainResponse;

        List<RequestMountain> requestMountain = requestMountainRepo.findByRequestMountain(u.getRoleName());

        requestMountainResponse = serviceDataList.listClimber(requestMountain);
        result.put("climber", requestMountainResponse);
        return result;
    }
}
