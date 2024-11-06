package com.tracker.backend.filters;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.tracker.backend.services.CustomUserDetailsService;
import com.tracker.backend.utils.JWTUtil;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements WebFilter {
    private final JWTUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    @SuppressWarnings("null")
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String token = extractToken(exchange);
        if (token != null && jwtUtil.extractUsername(token) != null) {
            return userDetailsService.findByUsername(jwtUtil.extractUsername(token))
                    .flatMap(userDetails -> {
                        if (jwtUtil.validateToken(token, userDetails.getUsername())) {
                            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities());
                            SecurityContext securityContext = new SecurityContextImpl(auth);
                            return chain.filter(exchange)
                                    .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext)));
                        }
                        return chain.filter(exchange);
                    });
        }
        return chain.filter(exchange);
    }

    private String extractToken(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        return (authHeader != null && authHeader.startsWith("Bearer ")) ? authHeader.substring(7) : null;
    }
}
