package com.h5.game.model.vo;


import com.h5.game.common.tools.db.base.BaseVo;
import com.h5.game.common.tools.validate.annotations.Validate;
import org.springframework.web.multipart.commons.CommonsMultipartFile;


/**
 * Created by 黄春怡 on 2017/4/10.
 */
public class GameVo extends BaseVo {

    private String gameName;//游戏名称

    private Integer gameTypeId;//游戏类型

    private String summary;//游戏简介

    private String indexName;//游戏首页名称

    @Validate
    private CommonsMultipartFile zip;//游戏压缩文件

    private CommonsMultipartFile icon;//游戏图标

    private CommonsMultipartFile screen;//游戏封面

    private CommonsMultipartFile[] previewPics;//游戏预览图

    private String version;//版本

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

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public CommonsMultipartFile getZip() {
        return zip;
    }

    public void setZip(CommonsMultipartFile zip) {
        this.zip = zip;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public CommonsMultipartFile getIcon() {
        return icon;
    }

    public void setIcon(CommonsMultipartFile icon) {
        this.icon = icon;
    }

    public CommonsMultipartFile[] getPreviewPics() {
        return previewPics;
    }

    public void setPreviewPics(CommonsMultipartFile[] previewPics) {
        this.previewPics = previewPics;
    }

    public CommonsMultipartFile getScreen() {
        return screen;
    }

    public void setScreen(CommonsMultipartFile screen) {
        this.screen = screen;
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }
}
