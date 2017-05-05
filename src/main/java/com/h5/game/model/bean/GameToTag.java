package com.h5.game.model.bean;

import com.h5.game.model.base.BaseBean;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by 黄春怡 on 2017/4/19.
 */
public class GameToTag extends BaseBean {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "gameId")
    private Game gameId;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "tagId")
    private GameTag tagId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Game getGameId() {
        return gameId;
    }

    public void setGameId(Game gameId) {
        this.gameId = gameId;
    }

    public GameTag getTagId() {
        return tagId;
    }

    public void setTagId(GameTag tagId) {
        this.tagId = tagId;
    }
}
