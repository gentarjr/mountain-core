package com.mountain.repo;

import com.mountain.entity.detail.Mountain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MountainRepo extends JpaRepository<Mountain, String> {

    Mountain findByMountainNameAndBasecampName(String mountainName, String basecampName);
}
