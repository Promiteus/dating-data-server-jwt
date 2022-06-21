package com.romanm.jwtservicedata.models.auth;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date regDate;
    private String email;
    private boolean verifiedEmail;
    private Short code;
    private String rememberToken;

    public AuthUser(String username, String password, List<GrantedAuthority> grantedAuthorities, boolean disabled) {
        this.username = username;
        this.password = password;
        this.grantedAuthorities = grantedAuthorities;
        this.disabled = disabled;
        this.email = username;

        this.regDate =new Date();
    }

    @Override
    public String toString() {
        return "AuthUser{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password=''" +
                ", grantedAuthorities=" + grantedAuthorities +
                ", disabled=" + disabled +
                ", regDate=" + regDate +
                ", email='" + email + '\'' +
                ", verifiedEmail=" + verifiedEmail +
                '}';
    }
}
