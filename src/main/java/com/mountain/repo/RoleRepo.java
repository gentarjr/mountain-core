package com.mountain.repo;

import com.mountain.entity.role.Role;
import com.mountain.entity.role.Role.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepo extends JpaRepository<Role, String> {
    Role findByName(ERole user);
}
