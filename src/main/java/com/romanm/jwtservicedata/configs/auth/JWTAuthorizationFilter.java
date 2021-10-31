package com.romanm.jwtservicedata.configs.auth;

import com.auth0.jwt.algorithms.Algorithm;
import com.romanm.jwtservicedata.constants.AuthenticationConfigConstants;
import com.romanm.jwtservicedata.models.auth.AuthUser;
import com.romanm.jwtservicedata.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import com.auth0.jwt.JWT;
import org.springframework.web.server.ResponseStatusException;

/**
 * Фильтр для идентификации токена по публичному ключу
 */
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
    private UserService userService;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, UserService userService) {
        super(authenticationManager);
        this.userService = userService;
    }

    /**
     *
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(AuthenticationConfigConstants.HEADER_STRING);

        if (header == null || !header.startsWith(AuthenticationConfigConstants.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    /**
     *
     * @param request
     * @return
     */
    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(AuthenticationConfigConstants.HEADER_STRING);
        if (token != null) {
            // parse the token.
            String username = null;
            try {
                username = JWT.require(Algorithm.HMAC512(AuthenticationConfigConstants.SECRET.getBytes()))
                        .build()
                        .verify(token.replace(AuthenticationConfigConstants.TOKEN_PREFIX, ""))
                        .getSubject();
            } catch (Exception e) { // Неверный токен или истекло его время действия
                AuthenticationConfigConstants.invalidToken(e.getMessage());
            }

            if (username != null) {
                AuthenticationConfigConstants.getDecodedUserMsg(username, request.getRequestURI(), request.getMethod());

                AuthUser authUser = this.userService.readUserByUsername(username);

                if (authUser == null) {
                    AuthenticationConfigConstants.getUserNotFoundMsg(username);
                    return null;
                }

                if (authUser.isDisabled()) {
                    AuthenticationConfigConstants.userBlocked(authUser.getUsername());
                    return null;
                }

                return new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
            }
            return null;
        }
        return null;
    }
}
