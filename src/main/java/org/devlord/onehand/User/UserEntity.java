package org.devlord.onehand.User;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.devlord.onehand.Entities.BaseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "users")
public class UserEntity extends BaseEntity implements UserDetails  {

    @Column(nullable = false)
    private String Firstname,Lastname;
    @Column(unique = true,nullable = false)
    private String username,email;
//    @Column(nullable = false)
    private String password;
    private Boolean isVerified = false;
    private String Avatar;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
