package com.h5.game.services.cache;

import com.h5.game.model.base.BaseBean;
import com.h5.game.model.bean.Admin;
import com.h5.game.model.bean.User;

import java.util.Date;

/**
 * Created by 黄春怡 on 2017/3/31.
 */
public class TokenCache extends BaseBean {

    private String token;

    private Admin admin;

    private User user;

    private Date outTime;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getOutTime() {
        return outTime;
    }

    public void setOutTime(Date outTime) {
        this.outTime = outTime;
    }
}
