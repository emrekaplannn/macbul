package com.macbul.platform.config;

import com.macbul.platform.util.JwtService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import java.io.IOException;

@Component
public class JwtAuthFilter extends org.springframework.web.filter.OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthFilter(JwtService jwtService, UserDetailsService uds){
        this.jwtService = jwtService;
        this.userDetailsService = uds;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        String token = (StringUtils.hasText(header) && header.startsWith("Bearer ")) ? header.substring(7) : null;

        if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                String username = jwtService.extractUsername(token);
                UserDetails user = userDetailsService.loadUserByUsername(username);
                if (jwtService.isTokenValid(token, user) && jwtService.isAccessToken(token)) {
                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            } catch (Exception ignored) {}
        }
        chain.doFilter(request, response);
    }
}
