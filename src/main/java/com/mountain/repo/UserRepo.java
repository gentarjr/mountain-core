package com.mountain.repo;

import com.mountain.entity.role.Role.ERole;
import com.mountain.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User, String> {
    User findByPhoneNumberAndRole(String phoneNumber, ERole role);

    User findByPhoneNumberOrUsernameOrEmailOrNikAndRole(String phoneNumber, String username, String email, String nik, ERole typeUser);

    User findByEmailAndRole(String email, ERole typeUser);

    User findByPhoneNumber(String phoneNumber);

    User findByUsername(String username);

    List<User> findByMountainIdAndRoleOrderByCreatedDateAsc(String mountainId, ERole role);
}
