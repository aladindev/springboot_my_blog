package com.aladin.springbootstudy.dto;

import javax.persistence.Entity;

public class DTO_USER_AUTH {
    private String user_id;
    private String auth_cd;
    private String auth_level;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
