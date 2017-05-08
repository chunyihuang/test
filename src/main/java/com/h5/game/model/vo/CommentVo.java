package com.h5.game.model.vo;

import com.h5.game.model.bean.GameTag;
import com.h5.game.common.tools.db.base.BaseVo;
import com.h5.game.common.tools.validate.annotations.Validate;

import java.util.Set;

/**
 * Created by 黄春怡 on 2017/4/14.
 */
public class CommentVo extends BaseVo {

    private Boolean download = false;//是否下载

    private Boolean liked = false;//是否喜欢（点赞）

    @Validate
    private Integer gameId;//关联游戏

    private Set<GameTag> tags;//标签

    private String commentInfo;//评论内容

    private Float score;//评分

    public Boolean getDownload() {
        return download;
    }

    public void setDownload(Boolean download) {
        this.download = download;
    }

    public Boolean getLiked() {
        return liked;
    }

    public void setLiked(Boolean liked) {
        this.liked = liked;
    }

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    public Set<GameTag> getTags() {
        return tags;
    }

    public void setTags(Set<GameTag> tags) {
        this.tags = tags;
    }

    public String getCommentInfo() {
        return commentInfo;
    }

    public void setCommentInfo(String commentInfo) {
        this.commentInfo = commentInfo;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }
}
