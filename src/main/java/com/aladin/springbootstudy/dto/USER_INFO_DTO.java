package com.aladin.springbootstudy.dto;

import java.util.Date;

public class USER_INFO_DTO {
    private String userId;
    private String secretKey;

    private Date createDtm;
    private Date changeDtm;

    public Date getCreateDtm() {
        return createDtm;
    }

    public void setCreateDtm(Date createDtm) {
        this.createDtm = createDtm;
    }

    public Date getChangeDtm() {
        return changeDtm;
    }

    public void setChangeDtm(Date changeDtm) {
        this.changeDtm = changeDtm;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
