package com.mountain.repo;

import com.mountain.entity.role.Role.ERole;
import com.mountain.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    Optional<User> findByPhoneNumberOrEmailOrIdCardAndRole(String phoneNumber, String email, String idCard, ERole roleUser);

    Optional<User> findByPhoneNumberAndRole(String phoneNumber, ERole roleUser);

    Optional<User> findByEmailAndRole(String email, ERole roleUser);

    Optional<User> findByPhoneNumberAndRoleName(String phoneNumber, String role);

    Optional<User> findByPhoneNumberOrEmailOrIdCardAndRoleName(String phoneNumber, String email, String idCard, String role);

    User findById(String id);
}
