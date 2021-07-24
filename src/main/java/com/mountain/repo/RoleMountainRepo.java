package com.mountain.repo;

import com.mountain.entity.role.RoleMountain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleMountainRepo extends JpaRepository<RoleMountain, Long> {
    RoleMountain findByRoleName(String rolesName);
}
