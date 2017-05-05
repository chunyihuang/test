package com.h5.game.model.base;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * Created by 黄春怡 on 2017/4/7.
 */
public class BaseBean implements Serializable {
    @JsonIgnore
    protected boolean del = false;
    @JsonIgnore
    public boolean isDel() {
        return del;
    }
    @JsonIgnore
    public void setDel(boolean del) {
        this.del = del;
    }
}
