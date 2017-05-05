package com.h5.game.model.bean;

import com.h5.game.common.tools.validate.annotations.Validate;
import com.h5.game.model.base.BaseBean;


import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * 游戏分类实体
 * Created by 黄春怡 on 2017/3/31.
 */
@Entity
@Table(name="h5_gameType",catalog = "h5")
public class GameType extends BaseBean {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id",unique = true,nullable = false)
    private Integer id;

    @Validate
    @Column(name = "name",unique = true,nullable = false)
    private String name;

    private String type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
