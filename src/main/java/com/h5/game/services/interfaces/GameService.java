package com.h5.game.services.interfaces;

import com.h5.game.dao.base.PageResults;
import com.h5.game.model.bean.Comment;
import com.h5.game.model.bean.Game;
import com.h5.game.model.bean.User;
import com.h5.game.model.vo.CommentVo;
import com.h5.game.model.vo.GameVo;
import com.h5.game.model.vo.QueryCommentVo;
import com.h5.game.model.vo.QueryGameVo;

import java.io.IOException;
import java.util.Map;

/**
 * Created by 黄春怡 on 2017/4/10.
 */
public interface GameService {

    //上传游戏
    Map uploadGame(GameVo gameVo, String gameDir, User user);

    //获取游戏详情
    Game getGameById(Integer id);

    //获取游戏列表
    PageResults listGames(QueryGameVo queryGameVo, Integer page, Integer rows);

    //根据评论列表
    PageResults<Comment> listComments(QueryCommentVo commentVo, Integer page, Integer rows);

    Integer countComments(QueryCommentVo commentVo);


    //删除游戏
    Boolean removeGame(Integer id);

    //评论游戏(点赞，评论，是否下载)
    Map publishComment(CommentVo commentVo, User user);

    //更改游戏
    Map updateGame(Game game);

    //删除评论
    boolean removeComment(Comment comment);

    void insertNodeToXml(String xmlPath);
    boolean apk(String apkName, String srcPath) throws IOException,InterruptedException;
    boolean runbat(String batPath, String... params);
}
