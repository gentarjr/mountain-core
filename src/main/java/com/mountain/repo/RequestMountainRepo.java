package com.mountain.repo;

import com.mountain.entity.detail.RequestMountain;
import com.mountain.entity.detail.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestMountainRepo extends JpaRepository<RequestMountain, Long> {
    List<RequestMountain> findByRequestMountain(String roleName);

    RequestMountain findById(String climberId);

    RequestMountain findByTransaction(Transaction tr);
}
