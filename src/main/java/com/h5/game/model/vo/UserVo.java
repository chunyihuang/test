package com.h5.game.model.vo;

import com.h5.game.common.tools.db.base.BaseVo;
import com.h5.game.common.tools.validate.annotations.Validate;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * Created by 黄春怡 on 2017/4/7.
 */
public class UserVo extends BaseVo{

    private Integer id;

    @Validate
    private String user;

    @Validate
    private String password;

    private String sex;//性别

    private CommonsMultipartFile avatar;//头像

    @Validate
    private String email;//邮箱

    private String wechat;//微信

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public CommonsMultipartFile getAvatar() {
        return avatar;
    }

    public void setAvatar(CommonsMultipartFile avatar) {
        this.avatar = avatar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }
}
