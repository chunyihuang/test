package com.h5.game.model.bean;

import com.h5.game.common.tools.validate.annotations.Validate;
import com.h5.game.model.base.BaseBean;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by 黄春怡 on 2017/5/8.
 */
public class SystemConfig extends BaseBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",unique = true,nullable = false)
    private Integer id;

    @Validate
    @Column(name = "param",unique = true,nullable = false)
    private String param;

    private String value;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
