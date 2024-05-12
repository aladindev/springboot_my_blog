package com.aladin.springbootstudy.dto;

import javax.persistence.Entity;
import java.util.Objects;

public class DTO_USER_INFO {
    private String user_id;
    private String secret_key;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
