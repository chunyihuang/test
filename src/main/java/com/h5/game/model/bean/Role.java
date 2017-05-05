package com.h5.game.model.bean;

import com.h5.game.model.base.BaseBean;

import javax.persistence.*;

/**
 * 角色表
 * Created by admin on 2016/8/4.
 */
@Entity
@Table(name = "h5_role",catalog = "h5")
public class Role extends BaseBean {

    //Fields

    private Integer id;

    private String code; //编码

    private String name; //名称

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
