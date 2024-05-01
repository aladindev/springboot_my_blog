package com.aladin.springbootstudy.dto;

import javax.persistence.Entity;
import java.util.Objects;

@Entity
public class USER_AUTH {
    private String user_id;
    private String auth_cd;
    private String auth_level;

    public USER_AUTH(String user_id, String auth_cd, String auth_level) {
        this.user_id = Objects.requireNonNull(user_id);
        this.auth_cd = Objects.requireNonNull(auth_cd);
        this.auth_level = Objects.requireNonNull(auth_level);
    }
}
