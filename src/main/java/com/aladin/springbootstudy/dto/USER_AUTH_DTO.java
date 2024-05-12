package com.aladin.springbootstudy.dto;

import java.util.Date;

public class USER_AUTH_DTO {
    private String userId;
    private String authCd;
    private String authLevel;
    private Date createDtm;
    private Date changeDtm;

    public String getAuthCd() {
        return authCd;
    }

    public void setAuthCd(String authCd) {
        this.authCd = authCd;
    }

    public String getAuthLevel() {
        return authLevel;
    }

    public void setAuthLevel(String authLevel) {
        this.authLevel = authLevel;
    }

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
}
