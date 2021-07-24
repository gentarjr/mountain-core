package com.mountain.spring.principal;

import com.mountain.entity.role.Role;
import com.mountain.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrincipalRepo extends JpaRepository<User, Long> {

    User findByPhoneNumberAndRole(String phoneNumber, Role.ERole role);

    User findByPhoneNumberAndRoleName(String phoneNumber, String role);
}
