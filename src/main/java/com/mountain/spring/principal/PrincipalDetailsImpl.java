package com.mountain.spring.principal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mountain.entity.role.Role.ERole;
import com.mountain.entity.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PrincipalDetailsImpl implements UserDetails {

    private static final long serialVersionUID = 1L;

    private String id;
    private String phoneNumber;
    private String mountainName;
    private String email;
    private String firstName;
    @JsonIgnore
    private String pin;
    private ERole role;
    private Timestamp createdDate;
    private Timestamp updatedDate;

    private Collection<? extends GrantedAuthority> authorities;

    public PrincipalDetailsImpl(String id, String phoneNumber, String email,
                                String firstName, String pin, ERole role,
                                Timestamp createdDate, Timestamp updatedDate,
                                List<GrantedAuthority> authorities) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.firstName = firstName;
        this.pin = pin;
        this.role = role;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.authorities = authorities;
    }


    public static UserDetails build(User user) {

        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name())).collect(Collectors.toList());

        return new PrincipalDetailsImpl(user.getId(), user.getPhoneNumber(), user.getEmail(), user.getFirstName(), user.getPin(),
                user.getRole(), user.getCreatedDate(), user.getUpdatedDate(), authorities);
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public String getId() {
        return id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public ERole getRole() {
        return role;
    }

    public String getMountainName() {
        return mountainName;
    }

    public String getPin() {
        return pin;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public Timestamp getUpdatedDate() {
        return updatedDate;
    }


    @Override
    public String getPassword() {
        return pin;
    }

    @Override
    public String getUsername() {
        return phoneNumber;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        PrincipalDetailsImpl user = (PrincipalDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
}
