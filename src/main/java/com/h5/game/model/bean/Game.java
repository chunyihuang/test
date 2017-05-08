package com.h5.game.model.bean;

import com.h5.game.model.base.BaseBean;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by 黄春怡 on 2017/3/31.
 */
@Entity
@Table(name="h5_game",catalog = "h5")
public class Game extends BaseBean {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id",unique = true,nullable = false)
    private Integer id;

    @Column(name = "gameName",unique = true,nullable = false)
    private String gameName;//游戏名称

    @ManyToOne(cascade= CascadeType.ALL)
    @JoinColumn(name = "gameTypeId",nullable = false)
    private GameType gameType;//游戏类型

    @ManyToOne(cascade= CascadeType.ALL)
    @JoinColumn(name = "userId",nullable = false)
    private User user;//上传者

    @ManyToOne(cascade= CascadeType.ALL)
    @JoinColumn(name = "adminId")
    private Admin checker;//审核者

    private String icon;//游戏图标
    private String screen;//游戏启动时候封面
    private String previewPics;//游戏预览图目录

    private String summary;//游戏简介
    private Float scord;//评分
    private Integer top; // 排行

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "game")
    private List<Comment> comments = new ArrayList<Comment>();// 评论
    private String size;//游戏大小(mb)
    private Float expenses = 1.0f;//资费
    private String version = "1.0";//版本
    @ManyToOne(cascade= CascadeType.ALL)
    @JoinColumn(name = "gameTagId",nullable = true)
    private GameTag gameTag;//标签

    private Integer checked = 2;//是否通过审核(1.通过 2.等待审核 3.审核失败)

    private Boolean commended = false;//是否推荐
    private String onlineUrl;//在线试玩入口地址
    private Boolean packageApk = false;//apk是否打包成功
    private String apkDowload;//apk下载地址

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="uploadDate")
    private Date uploadDate;//上传日期


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public GameType getGameType() {
        return gameType;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Float getScord() {
        return scord;
    }

    public void setScord(Float scord) {
        this.scord = scord;
    }

    public Integer getTop() {
        return top;
    }

    public void setTop(Integer top) {
        this.top = top;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Float getExpenses() {
        return expenses;
    }

    public void setExpenses(Float expenses) {
        this.expenses = expenses;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public GameTag getGameTag() {
        return gameTag;
    }

    public void setGameTag(GameTag gameTag) {
        this.gameTag = gameTag;
    }

    public Integer getChecked() {
        return checked;
    }

    public void setChecked(Integer checked) {
        this.checked = checked;
    }

    public Boolean getCommended() {
        return commended;
    }

    public void setCommended(Boolean commended) {
        this.commended = commended;
    }

    public String getPreviewPics() {
        return previewPics;
    }

    public void setPreviewPics(String previewPics) {
        this.previewPics = previewPics;
    }

    public String getOnlineUrl() {
        return onlineUrl;
    }

    public void setOnlineUrl(String onlineUrl) {
        this.onlineUrl = onlineUrl;
    }

    public Boolean getPackageApk() {
        return packageApk;
    }

    public void setPackageApk(Boolean packageApk) {
        this.packageApk = packageApk;
    }

    public String getApkDowload() {
        return apkDowload;
    }

    public void setApkDowload(String apkDowload) {
        this.apkDowload = apkDowload;
    }

    public Boolean getDel() {
        return del;
    }

    public void setDel(Boolean del) {
        this.del = del;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getScreen() {
        return screen;
    }

    public void setScreen(String screen) {
        this.screen = screen;
    }


    public Admin getChecker() {
        return checker;
    }

    public void setChecker(Admin checker) {
        this.checker = checker;
    }
}
