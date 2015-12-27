package com.messake.messake.core;

/**
 *
 * Created by messake on 2015/12/26.
 */
public class ClientLoginMessage {
    private String token;
    private int userId;

    public String getToken() {
        return token;
    }

    public int getUserId() {
        return userId;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
