package com.mountain.spring.principal;

import com.mountain.entity.role.Role;
import com.mountain.entity.user.User;
import com.mountain.library.domain.ErrCode;
import com.mountain.library.exceptions.NonexistentEntityException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalServiceImpl implements UserDetailsService {

    private final PrincipalRepo principalRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Role.ERole role = Role.ERole.USER;
        User user = principalRepo.findByPhoneNumberAndRole(username, role);
        if (user == null) {
            throw new NonexistentEntityException(ErrCode.INF_USEREMPTY, "User not listed");
        }
        return PrincipalDetailsImpl.build(user);
    }
}
