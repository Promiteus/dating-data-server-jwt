package com.romanm.jwtservicedata.configs.auth;


import com.romanm.jwtservicedata.services.UserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;


/**
 * Фильтр для идентификации токена по публичному ключу
 */
public class JWTAuthorizationFilter implements WebFilter {
    private UserService userService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        UsernamePasswordAuthenticationToken authentication = getAuthentication(exchange);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return chain.filter(exchange);
    }


    public JWTAuthorizationFilter(UserService userService) {
        this.userService = userService;
    }


  /*  @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(AuthenticationConfigConstants.HEADER_STRING);

        if (header == null || !header.startsWith(AuthenticationConfigConstants.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }*/


    private UsernamePasswordAuthenticationToken getAuthentication(ServerWebExchange exchange) {
        /*String token = request.getHeader(AuthenticationConfigConstants.HEADER_STRING);
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
        }*/
        return null;
    }
}
