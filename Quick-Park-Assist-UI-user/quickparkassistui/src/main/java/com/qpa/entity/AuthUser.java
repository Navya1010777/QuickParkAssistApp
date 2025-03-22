package com.qpa.entity;

public class AuthUser {

    private Long authId;

    private String username;

    private String password;

    private UserInfo user;

    public AuthUser() {
    }

    public AuthUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Long getAuthId() {
        return authId;
    }

    public void setAuthId(Long authId) {
        this.authId = authId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }
}
