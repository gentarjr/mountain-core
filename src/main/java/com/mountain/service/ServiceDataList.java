package com.mountain.service;

import com.mountain.domain.response.EquipmentResponse;
import com.mountain.domain.response.MemberResponse;
import com.mountain.domain.response.RequestMountainResponse;
import com.mountain.domain.response.TransactionResponse;
import com.mountain.entity.detail.Member;
import com.mountain.entity.detail.RequestMountain;
import com.mountain.library.helper.DateUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceDataList {

    public List<MemberResponse> userMemberInfo(List<Member> member) {
        List<MemberResponse> result = new ArrayList<>();
        for (Member m : member) {
            MemberResponse data = new MemberResponse();
            data.setId(m.getId());
            data.setIdCard(m.getIdCard());
            data.setName(m.getName());
            data.setPhoneNumber(m.getPhoneNumber());
            data.setProvinsi(m.getProvinsi());
            data.setKota(m.getKota());
            data.setKecamatan(m.getKecamatan());
            data.setRtRw(m.getRtRw());
            data.setCreatedDate(DateUtils.formatDateTimeOrEmptyString(m.getCreatedDate()));
            data.setUpdatedDate(DateUtils.formatDateTimeOrEmptyString(m.getUpdatedDate()));
            result.add(data);
        }
        return result;
    }

    public List<RequestMountainResponse> listClimber(List<RequestMountain> requestMountain) {
        List<RequestMountainResponse> result = new ArrayList<>();
        for (RequestMountain rm : requestMountain) {
            RequestMountainResponse data = new RequestMountainResponse();
            List<MemberResponse> memberResponse = new ArrayList<>();
            data.setId(rm.getId());
            data.setTotalClimber(rm.getTotalClimber());
            data.setRequestMountain(rm.getRequestMountain());
            data.setCreatedDate(DateUtils.formatDateTimeOrEmptyString(rm.getCreatedDate()));
            data.setUpdatedDate(DateUtils.formatDateTimeOrEmptyString(rm.getUpdatedDate()));
            for (Member m : rm.getMember()) {
                MemberResponse data2 = new MemberResponse();
                data2.setId(m.getId());
                data2.setIdCard(m.getIdCard());
                data2.setName(m.getName());
                data2.setPhoneNumber(m.getPhoneNumber());
                data2.setProvinsi(m.getProvinsi());
                data2.setKota(m.getKota());
                data2.setKecamatan(m.getKecamatan());
                data2.setRtRw(m.getRtRw());
                data2.setCreatedDate(DateUtils.formatDateTimeOrEmptyString(m.getCreatedDate()));
                data2.setUpdatedDate(DateUtils.formatDateTimeOrEmptyString(m.getUpdatedDate()));
                memberResponse.add(data2);
            }
            TransactionResponse data3 = new TransactionResponse();
            data3.setId(rm.getTransaction().getId());
            data3.setMountain(rm.getTransaction().getMountain());
            data3.setPayment(rm.getTransaction().getPayment());
            data3.setStatus(rm.getStatus());
            data3.setReason(rm.getReason());
            data3.setDate(rm.getTransaction().getClimberDate());
            data3.setCreatedDate(DateUtils.formatDateTimeOrEmptyString(rm.getCreatedDate()));
            data3.setUpdatedDate(DateUtils.formatDateTimeOrEmptyString(rm.getUpdatedDate()));
            EquipmentResponse data4 = new EquipmentResponse();
            data4.setId(rm.getEquipment().getId());
            data4.setBackPack(rm.getEquipment().getBackPack());
            data4.setFlashLight(rm.getEquipment().getFlashLight());
            data4.setFood(rm.getEquipment().getFood());
            data4.setMattress(rm.getEquipment().getMattress());
            data4.setNesting(rm.getEquipment().getNesting());
            data4.setRainCoat(rm.getEquipment().getRainCoat());
            data4.setStove(rm.getEquipment().getStove());
            data4.setWater(rm.getEquipment().getWater());
            data4.setTent(rm.getEquipment().getTent());
            data.setEquipment(data4);
            data.setMember(memberResponse);
            data.setTransactions(data3);
            result.add(data);
        }
        return result;
    }

    public List<RequestMountainResponse> listRequestMountain(List<RequestMountain> requestMountain) {
        List<RequestMountainResponse> result = new ArrayList<>();
        for (RequestMountain rm : requestMountain) {
            RequestMountainResponse data = new RequestMountainResponse();
            List<MemberResponse> memberResponse = new ArrayList<>();
            data.setId(rm.getId());
            data.setTotalClimber(rm.getTotalClimber());
            data.setRequestMountain(rm.getRequestMountain());
            data.setCreatedDate(DateUtils.formatDateTimeOrEmptyString(rm.getCreatedDate()));
            data.setUpdatedDate(DateUtils.formatDateTimeOrEmptyString(rm.getUpdatedDate()));
            for (Member m : rm.getMember()) {
                MemberResponse data2 = new MemberResponse();
                data2.setId(m.getId());
                data2.setIdCard(m.getIdCard());
                data2.setName(m.getName());
                data2.setPhoneNumber(m.getPhoneNumber());
                data2.setProvinsi(m.getProvinsi());
                data2.setKota(m.getKota());
                data2.setKecamatan(m.getKecamatan());
                data2.setRtRw(m.getRtRw());
                data2.setCreatedDate(DateUtils.formatDateTimeOrEmptyString(m.getCreatedDate()));
                data2.setUpdatedDate(DateUtils.formatDateTimeOrEmptyString(m.getUpdatedDate()));
                memberResponse.add(data2);
            }
            EquipmentResponse equipmentResponse = new EquipmentResponse();
            equipmentResponse.setId(rm.getEquipment().getId());
            equipmentResponse.setBackPack(rm.getEquipment().getBackPack());
            equipmentResponse.setFlashLight(rm.getEquipment().getFlashLight());
            equipmentResponse.setFood(rm.getEquipment().getFood());
            equipmentResponse.setMattress(rm.getEquipment().getMattress());
            equipmentResponse.setNesting(rm.getEquipment().getNesting());
            equipmentResponse.setRainCoat(rm.getEquipment().getRainCoat());
            equipmentResponse.setStove(rm.getEquipment().getStove());
            equipmentResponse.setWater(rm.getEquipment().getWater());
            equipmentResponse.setTent(rm.getEquipment().getTent());
            data.setEquipment(equipmentResponse);
            data.setMember(memberResponse);
            result.add(data);
        }
        return result;
    }
}
