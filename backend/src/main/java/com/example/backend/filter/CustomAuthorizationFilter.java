package com.example.backend.filter;

import com.example.backend.util.JwtUtil;
import jakarta.json.Json;
import jakarta.json.JsonObjectBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    //    this enables you to access the resource if you have the right authority.. access token needs to be sent as header
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().equals("/api/login") || request.getServletPath().equals("/api/token/refresh")
                || request.getServletPath().equals("/api/register") || request.getServletPath().equals("/api/verify")) {
            filterChain.doFilter(request, response);
        } else {
            String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                try {
                    UsernamePasswordAuthenticationToken authenticationToken =
                            JwtUtil.createAuthorizationToken(authorizationHeader);

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request, response);
                } catch (Exception e) {
                    log.info("Error logging in: {}", e.getMessage());
                    response.setHeader("error", e.getMessage());
//                    response.sendError(HttpStatus.FORBIDDEN.value());
                    response.setStatus(HttpStatus.FORBIDDEN.value());

                    JsonObjectBuilder objectBuilder = Json.createObjectBuilder()
                            .add("error_message", e.getMessage());
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.getOutputStream().write(objectBuilder.build().toString().getBytes());
                }
            } else {
                filterChain.doFilter(request, response);
            }
        }
    }
}
