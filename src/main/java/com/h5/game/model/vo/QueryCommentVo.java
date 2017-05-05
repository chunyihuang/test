package com.h5.game.model.vo;

import com.h5.game.common.tools.db.base.BaseVo;

/**
 * Created by 黄春怡 on 2017/5/1.
 */
public class QueryCommentVo extends BaseVo{

    private Boolean download;//是否下载
    private Boolean liked;//是否喜欢（点赞）
    private Integer gameId;//关联游戏
    private Integer userId;//关联用户
    private String uploadDate;//上传日期
    private Boolean comment;//是否填写评论

    public Boolean getComment() {
        return comment;
    }

    public void setComment(Boolean comment) {
        this.comment = comment;
    }

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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }
}
