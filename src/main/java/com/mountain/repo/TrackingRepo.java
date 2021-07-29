package com.mountain.repo;

import com.mountain.entity.detail.Tracking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrackingRepo extends JpaRepository<Tracking, String> {
}
