package com.mountain.service;

import com.mountain.domain.response.MountainResponse;
import com.mountain.entity.detail.Mountain;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceDataList {

    public List<MountainResponse> listMountain(List<Mountain> mountain) {
        List<MountainResponse> result = new ArrayList<>();
        for(Mountain m : mountain){
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
}
