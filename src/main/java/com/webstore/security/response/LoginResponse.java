package com.webstore.security.response;

import org.springframework.http.ResponseCookie;

import java.util.List;

public class LoginResponse {
    private Long id;
    private String username;
    private List<String> roles;
    private ResponseCookie jwtCookie;


    public LoginResponse(Long id, ResponseCookie jwtCookie, String username, List<String> roles) {
        this.id = id;
        this.jwtCookie = jwtCookie;
        this.username = username;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ResponseCookie getJwtCookie() {
        return jwtCookie;
    }

    public void setJwtCookie(ResponseCookie jwtCookie) {
        this.jwtCookie = jwtCookie;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

}
