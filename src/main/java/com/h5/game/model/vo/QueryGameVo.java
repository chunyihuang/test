package com.h5.game.model.vo;

import com.h5.game.common.tools.db.base.BaseVo;

/**
 * Created by 黄春怡 on 2017/4/13.
 */
public class QueryGameVo extends BaseVo {


    private String gameName;//游戏名称

    private Integer gameTypeId;//游戏类型

    private Integer userId;//上传者

    private Integer gameTagId;//标签

    private Boolean checked;//是否通过审核

    private Boolean commended;//是否推荐

    private String uploadDate;//上传日期

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public Integer getGameTypeId() {
        return gameTypeId;
    }

    public void setGameTypeId(Integer gameTypeId) {
        this.gameTypeId = gameTypeId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getGameTagId() {
        return gameTagId;
    }

    public void setGameTagId(Integer gameTagId) {
        this.gameTagId = gameTagId;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public Boolean getCommended() {
        return commended;
    }

    public void setCommended(Boolean commended) {
        this.commended = commended;
    }
}
