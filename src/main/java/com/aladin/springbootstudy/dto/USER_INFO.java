package com.aladin.springbootstudy.dto;

import javax.persistence.Entity;
import java.util.Objects;

@Entity
public class USER_INFO {
    private String user_id;
    private String secret_key;

    public USER_INFO(String user_id, String secret_key) {
        this.user_id = Objects.requireNonNull(user_id);
        this.secret_key = Objects.requireNonNull(secret_key);
    }
}
