package com.romanm.jwtservicedata.models.auth;

import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class GrantedAuthority implements Serializable {
    private String id;
    private String role;
    private AuthUser user;
}
