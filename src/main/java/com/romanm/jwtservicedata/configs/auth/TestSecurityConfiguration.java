package com.romanm.jwtservicedata.configs.auth;


import com.romanm.jwtservicedata.services.UserServiceV1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * Класс-конфигурация настроек безопасности для тестирования конечных точек без JWT
 */
@TestConfiguration
@EnableReactiveMethodSecurity
@Profile(value = {"test"})
public class TestSecurityConfiguration {
    private final UserServiceV1 userService;

    @Autowired
    public TestSecurityConfiguration(UserServiceV1 userService) {
        this.userService = userService;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http, AuthenticationManager authenticationManager) {
        return http
                .csrf().disable()
                .cors().disable()
                .httpBasic().disable()
                .formLogin().disable()
                .logout().disable()
                .authorizeExchange().anyExchange().permitAll().and().build();
    }
}
