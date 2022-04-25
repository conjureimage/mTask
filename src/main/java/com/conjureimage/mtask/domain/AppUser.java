package com.conjureimage.mtask.domain;

import com.conjureimage.mtask.domain.enums.Role;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "app_user")
@Data
public class AppUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", length = 100, nullable = false)
    private String username;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", length = 20, nullable = false)
    private Role role;

    @Column(name = "email", length = 100, nullable = false, unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    private boolean locked = false;
    private boolean enabled = true;

    public static Builder getBuilder() {
        return new Builder();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority =
                new SimpleGrantedAuthority(role.name());
        return Collections.singletonList(authority);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public static class Builder {

        private Long id;

        private String username;

        private String password;

        private Role role;

        private String email;

        private List<GrantedAuthority> authorities;

        private String imageUrl;

        public Builder() {
            this.authorities = new ArrayList<>();
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder password(String password) {
            if (password == null) {
                password = "SocialUser";
            }

            this.password = password;
            return this;
        }

        public Builder role(Role role) {
            this.role = role;

            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.toString());
            this.authorities.add(authority);

            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public AppUser build() {
            AppUser appUser = new AppUser();
            appUser.username = username;
            appUser.password = password;
            appUser.id = id;
            appUser.role = role;
            appUser.email = email;

            return appUser;
        }
    }
}