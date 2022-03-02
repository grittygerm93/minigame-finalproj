package com.example.backend.filter;

import com.example.backend.util.JwtUtil;
import jakarta.json.Json;
import jakarta.json.JsonObjectBuilder;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//        must send as form-url-encoded
//        can do an object mapper here as well..
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authenticationToken);
    }

    //    check the response after successful login.. header should have refresh and access token
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        User user = (User) authResult.getPrincipal();
//        usually this is saved somewhere secure.. encrypt it there and decrypt it for use here
//        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());

//        username has to be unique since using it as subject
        String access_token = JwtUtil.createFirstAccessToken(user, request);
        String refresh_token = JwtUtil.createRefreshToken(user, request);


        JsonObjectBuilder objectBuilder = Json.createObjectBuilder()
                .add("access_token", access_token)
                .add("refresh_token", refresh_token);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().write(objectBuilder.build().toString().getBytes());
    }
}