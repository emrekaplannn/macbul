package com.macbul.platform.config;

import com.macbul.platform.util.JwtService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthFilter.class);

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthFilter(JwtService jwtService, UserDetailsService uds) {
        this.jwtService = jwtService;
        this.userDetailsService = uds;
    }

    /**
     * Bu uçlara (ve OPTIONS preflight’a) filtre uygulanmaz.
     * SecurityConfig’te permitAll verdiğin yollarla uyumlu tut.
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        final String uri = request.getRequestURI();
        final String method = request.getMethod();

        if (HttpMethod.OPTIONS.matches(method)) return true; // CORS preflight

        return uri.startsWith("/v1/auth/")
                || uri.startsWith("/v3/api-docs")
                || uri.equals("/swagger-ui.html")
                || uri.startsWith("/swagger-ui")
                || uri.equals("/actuator/health")
                || uri.startsWith("/v1/matches/"); // public ise
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        String token = (StringUtils.hasText(header) && header.startsWith("Bearer "))
                ? header.substring(7)
                : null;

        // Context boşsa ve geçerli bir bearer header'ı varsa doğrula
        if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                String username = jwtService.extractUsername(token);
                if (StringUtils.hasText(username) && jwtService.isAccessToken(token)) {
                    UserDetails user = userDetailsService.loadUserByUsername(username);

                    if (jwtService.isTokenValid(token, user)) {
                        UsernamePasswordAuthenticationToken auth =
                                new UsernamePasswordAuthenticationToken(
                                        user, null, user.getAuthorities());
                        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                }
            } catch (JwtException | IllegalArgumentException ex) {
                // Geçersiz/bozuk token: context’e dokunma; exceptionHandling 401 üretecek
                log.debug("Invalid JWT: {}", ex.getMessage());
            } catch (UsernameNotFoundException ex) {
                // Token geçerli göründü ama kullanıcı bulunamadı
                log.debug("User not found for JWT subject: {}", ex.getMessage());
            } catch (Exception ex) {
                // Beklenmeyen durum – zinciri bozmadan logla
                log.error("Unexpected error in JwtAuthFilter", ex);
            }
        }

        chain.doFilter(request, response);
    }
}
