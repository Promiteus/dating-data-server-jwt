package com.romanm.jwtservicedata.configs.auth;

import com.romanm.jwtservicedata.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableReactiveMethodSecurity
public class SecurityConfiguration  /*extends WebSecurityConfigurerAdapter*/ {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserService userService;

    @Autowired
    public SecurityConfiguration(BCryptPasswordEncoder bCryptPasswordEncoder, UserService userService) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userService = userService;
    }

    @Bean
    SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) throws Exception {
        return http.cors().and().csrf().disable().authorizeExchange(
                authorize -> authorize.anyExchange().authenticated()
        ).build();
    }

   /* @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().authorizeRequests()
                //.antMatchers(HttpMethod.POST, AuthenticationConfigConstants.SIGN_UP_URL).permitAll()
                .anyRequest().authenticated()
                .and()
                //.addFilter(new JWTAuthorizationFilter(authenticationManager(), this.userService))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }*/


}
