package org.knpkid.kms.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Data
@Entity
@Table(name = "admin")
public class Admin implements UserDetails {

    @Id
    private String username;

    private String password;

    private String name;

    @OneToOne
    @JoinColumn(name = "image", referencedColumnName = "id")
    private Image image;

    @OneToMany(mappedBy = "admin")
    private transient List<Article> articles;

    @OneToMany(mappedBy = "admin")
    private transient List<Quote> quotes;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
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
}
