package com.h5.game.model.bean;

import com.h5.game.model.base.BaseBean;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by 黄春怡 on 2017/3/31.
 */
@Entity
@Table(name="h5_comment",catalog = "h5")
public class Comment extends BaseBean {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;
    private Boolean download = false;//是否下载
    private Boolean liked = false;//是否喜欢（点赞）
    private Float score;//评分


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "gameId")
    private Game game;//关联游戏
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userId")
    private User user;//关联用户

   /* @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "h5_gameTag",
            joinColumns = {@JoinColumn(name = "comment_id",referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id",referencedColumnName = "id")})*/
    /*private Set<GameTag> tag_id = new HashSet<GameTag>();*/

    private Boolean comment = false;//是否填写评论

    @Type(type = "text")
    private String commentInfo;//评论内容

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="uploadDate")
    private Date uploadDate;//上传日期

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCommentInfo() {
        return commentInfo;
    }

    public void setCommentInfo(String commentInfo) {
        this.commentInfo = commentInfo;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public Boolean getComment() {
        return comment;
    }

    public void setComment(Boolean comment) {
        this.comment = comment;
    }
}
