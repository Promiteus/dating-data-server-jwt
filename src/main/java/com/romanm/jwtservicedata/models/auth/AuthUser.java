package com.romanm.jwtservicedata.models.auth;

import lombok.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class AuthUser implements Serializable {

    private String id;
    private String username;
    private String password;
    private List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
    private boolean disabled;

    public AuthUser(String username, String password, List<GrantedAuthority> grantedAuthorities, boolean disabled) {
        this.username = username;
        this.password = password;
        this.grantedAuthorities = grantedAuthorities;
        this.disabled = disabled;
    }

    @Override
    public String toString() {
        return "AuthUser{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password=''" +
                ", grantedAuthorities=" + grantedAuthorities +
                ", disabled=" + disabled +
                '}';
    }
}
