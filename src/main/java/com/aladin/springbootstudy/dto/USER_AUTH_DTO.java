package com.aladin.springbootstudy.dto;

public class USER_AUTH_DTO {
    private String userId;
    private String authCd;
    private String authLevel;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
