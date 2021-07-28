package com.mountain.service;

import com.mountain.domain.response.BasecampResponse;
import com.mountain.domain.response.MountainResponse;
import com.mountain.domain.response.StatusResponse;
import com.mountain.domain.response.UserResponse;
import com.mountain.entity.detail.Basecamp;
import com.mountain.entity.detail.Mountain;
import com.mountain.entity.user.Status;
import com.mountain.entity.user.User;
import com.mountain.library.helper.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceDataList {

    public List<MountainResponse> listMountain(List<Mountain> mountain) {
        List<MountainResponse> result = new ArrayList<>();
        for (Mountain m : mountain) {
            MountainResponse data = new MountainResponse();
            data.setId(m.getId());
            data.setPhoto("https://localhost:9009/v1/img/" + m.getId() + "/mountain");
            data.setMountainName(m.getMountainName());
            data.setDescription(m.getMountainName());
            data.setHeight(m.getHeight());
            data.setFullAddress(m.getFullAddress());
            result.add(data);
        }
        return result;
    }

    public List<BasecampResponse> listBasecamp(List<Basecamp> basecamp) {
        List<BasecampResponse> result = new ArrayList<>();
        for (Basecamp b : basecamp) {
            BasecampResponse data = new BasecampResponse();
            data.setId(b.getId());
            data.setBasecampName(b.getBasecampName());
            data.setDescription(b.getDescription());
            data.setPhoto("https://localhost:9009/v1/img/" + b.getBasecampName() + "/basecamp");
            data.setRegulation(b.getRegulation());
            data.setFullAddress(b.getFullAddress());
            data.setPrice(b.getPrice());
            data.setTotalClimber(b.getTotalClimber());
            data.setRequestClimber(DateUtils.formatDateOrEmptyString(b.getRequestClimber()));
            data.setCreatedBy(b.getCreatedBy());
            data.setCreatedDate(DateUtils.formatDateTimeOrEmptyString(b.getCreatedDate()));
            data.setUpdatedBy(b.getUpdatedBy());
            data.setUpdatedDate(DateUtils.formatDateTimeOrEmptyString(b.getUpdatedDate()));
            result.add(data);
        }
        return result;
    }

    public List<UserResponse> listRanger(List<User> user) {
        List<UserResponse> result = new ArrayList<>();
        for (User u : user) {
            UserResponse data = new UserResponse();
            data.setId(u.getId());
            data.setNik(u.getNik());
            data.setPhoneNumber(u.getPhoneNumber());
            data.setUsername(u.getUsername());
            data.setPhoto("https://localhost:9009/v1/img" + u.getId() + "/user");
            data.setFirstName(u.getFirstName());
            data.setLastName(u.getLastName());
            data.setAddress(u.getAddress());
            data.setEmail(u.getEmail());
            data.setIsDeleted(u.getIsDeleted());
            data.setCreatedDate(DateUtils.formatDateTimeOrEmptyString(u.getCreatedDate()));
            data.setUpdatedDate(DateUtils.formatDateTimeOrEmptyString(u.getUpdatedDate()));
            result.add(data);
        }
        return result;
    }

    public List<StatusResponse> listStatusMountain(List<Status> status) {
        List<StatusResponse> result = new ArrayList<>();
        for(Status s : status){
            StatusResponse data = new StatusResponse();
            data.setId(s.getId());
            data.setStatus(s.getStatus());
            data.setUsername(s.getUsername());
            data.setRole(s.getRole());
            data.setPhoto("https://localhost:9009/v1/img" + s.getId() + "/status");
            data.setCreatedDate(DateUtils.formatDateTimeOrEmptyString(s.getCreatedDate()));
            result.add(data);
        }
        return result;
    }
}
