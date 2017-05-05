package com.h5.game.model.bean;

import com.h5.game.model.base.BaseBean;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by 黄春怡 on 2017/4/10.
 */
@Entity
@Table(name = "h5_previewPic",catalog = "h5")
public class PreviewPic extends BaseBean {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id",unique = true,nullable = false)
    private Integer id;

    @ManyToOne(cascade= CascadeType.ALL)
    @JoinColumn(name = "gameId",nullable = false)
    private Game game;//游戏

    private String previewPicUrl;//游戏预览图的地址

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public String getPreviewPicUrl() {
        return previewPicUrl;
    }

    public void setPreviewPicUrl(String previewPicUrl) {
        this.previewPicUrl = previewPicUrl;
    }
}
