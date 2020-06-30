package com.david0926.drop.model;

import java.io.Serializable;
import java.util.List;

public class TokenModel implements Serializable {

    private String access, refresh;

    public TokenModel(){}

    public TokenModel(String access, String refresh) {
        this.access = access;
        this.refresh = refresh;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public String getRefresh() {
        return refresh;
    }

    public void setRefresh(String refresh) {
        this.refresh = refresh;
    }
}
