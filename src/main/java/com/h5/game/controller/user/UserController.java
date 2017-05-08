package com.h5.game.controller.user;

import com.h5.game.common.constants.Constants;
import com.h5.game.common.tools.BaseUtil;
import com.h5.game.common.tools.validate.annotations.RequestValidate;
import com.h5.game.common.tools.validate.annotations.Validate;
import com.h5.game.controller.BaseController;

import com.h5.game.dao.base.PageResults;
import com.h5.game.model.bean.Comment;
import com.h5.game.model.bean.User;
import com.h5.game.model.vo.*;
import com.h5.game.services.interfaces.AuthService;
import com.h5.game.services.interfaces.GameService;
import com.h5.game.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by 黄春怡 on 2017/4/7.
 */
@Controller
@RequestMapping(value = "/player")
public class UserController extends BaseController{

    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;
    @Autowired
    private GameService gameService;

    @RequestMapping(value = "/addOrChangeUser",method = RequestMethod.POST)
    @ResponseBody
    public Map register(@RequestValidate UserVo userVo, HttpSession session){
        return userService.saveOrUpdateUser(userVo,session);
    }

    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @ResponseBody
    public Map update(@RequestValidate UserVo userVo, HttpSession session){

        if(null == userVo.getId()){
            return buildReturnMap(false,"修改失败！");
        }
        return userService.saveOrUpdateUser(userVo,session);
    }

    @RequestMapping(value = "/login",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> login(@Validate String userName, @Validate String password, HttpServletRequest request){
        Map<String,Object> map = buildReturnMap(false,"");
        User user = userService.getByUserName(userName);
        if(null != user){
            //如果没有MD5加密，则直接比对
            //password = SercurityCode.MD5(password);
            if(user.getPassword().equals(password)){
                String token = BaseUtil.getRandomToken(32);
                while( authService.getTokenCacheByToken(token) != null){
                    token = BaseUtil.getRandomToken(32);//直到生成一串不存在的token为止
                }
                authService.saveTokenCache(token,user);
                request.getSession().setAttribute(Constants.SESSION_USER,user);
                map.put("token",token);
                map.put("status", true);
                map.put("reason", "登录成功");
                map.put("detail",user);
            } else {
                map.put("status", false);
                map.put("reason", "操作失败，密码错误");
            }
        } else {
            map.put("status", false);
            map.put("reason", "操作失败，找不到该用户");
        }
        return map;
    }


    @RequestMapping(value = "/getUser",method = RequestMethod.GET)
    @ResponseBody
    public Map getUser(@Validate Integer id){
        User user = userService.getUserById(id);
        if(null != user){
            Map result = buildReturnMap(true,"");
            result.put("data",user);
            return result;
        }else {
            return buildReturnMap(false,"没有找到该用户！");
        }

    }

    @RequestMapping(value = "/delComment",method = RequestMethod.POST)
    @ResponseBody
    public Map delComment(Comment comment){
        if(null != comment && comment.getUser().equals(Constants.SESSION_USER)){
            gameService.removeComment(comment);
            return buildReturnMap(true,"操作成功！");
        }else {
            return buildReturnMap(false,"操作失败！");
        }

    }

    @RequestMapping(value = "/uploadGame",method = RequestMethod.POST)
    @ResponseBody
    public Map uploadGame(@RequestValidate GameVo gameVo, HttpSession session) {
        User user = authService.getUser(session);
        String gameDir = session.getServletContext().getRealPath("/");
        return gameService.uploadGame(gameVo,gameDir,user);
    }

    @RequestMapping(value = "/uploadComment",method = RequestMethod.POST)
    @ResponseBody
    public Map uploadComment(@RequestValidate CommentVo commentVo, HttpSession session) {
        User user = (User)session.getAttribute(Constants.SESSION_USER);
        if(null == user){
            return buildReturnMap(false,"请先登录！");
        }
        return gameService.publishComment(commentVo,user);
    }


    @RequestMapping(value = "/getPersonalInfo",method = RequestMethod.GET)
    @ResponseBody
    public Map getPersonalInfo(@Validate Integer id){
        Map result = buildReturnMap(false,"");
        int dowload;
        int upload = 0;
        int comment;
        User user = userService.getUserById(id);
        if(null != user){
            QueryCommentVo queryCommentVo = new QueryCommentVo();
            queryCommentVo.setUserId(user.getId());
            queryCommentVo.setComment(true);
            Integer count_comments = gameService.countComments(queryCommentVo);
            comment = count_comments==null?0:count_comments;
            queryCommentVo = new QueryCommentVo();
            queryCommentVo.setDownload(true);
            Integer count_dowload = gameService.countComments(queryCommentVo);
            dowload = count_dowload==null?0:count_dowload;
            //queryCommentVo = new QueryCommentVo();
            //queryCommentVo.setUserId(user.getId());
            //queryCommentVo.setLiked(true);
            //Integer count_liked = gameService.countComments(queryCommentVo);

            QueryGameVo queryGameVo = new QueryGameVo();
            queryGameVo.setUserId(id);
            PageResults pageResults = gameService.listGames(queryGameVo,null,null);
            if(null != pageResults){
                upload = pageResults.getTotalCount();
            }
            result.put("status",true);
            result.put("count_comments",comment);
            result.put("count_dowload",dowload);
            result.put("count_upload",upload);
        }
        return result;

    }

    @RequestMapping(value = "/record_upload",method = RequestMethod.GET)
    @ResponseBody
    public Map record_upload(@Validate Integer id,@Validate Integer status,Integer page,Integer rows) {
        //status(1.已经审核 2.未审核 3.审核失败)
        Map result = buildReturnMap(false, "");
        User user = userService.getUserById(id);
        if (null != user) {
            QueryGameVo queryGameVo = new QueryGameVo();
            queryGameVo.setUserId(id);
            queryGameVo.setChecked(status);
            PageResults pageResults = gameService.listGames(queryGameVo, page, rows);
            if (null != pageResults && pageResults.getTotalCount() > 0) {
                result.put("status", true);
                result.put("data", pageResults.getData());
                result.put("count", pageResults.getTotalCount());
            } else {
                result.put("reason", "还没有任何数据！");
            }
        }
        return result;


    }

    @RequestMapping(value = "/record_download",method = RequestMethod.GET)
    @ResponseBody
    public Map record_download(@Validate Integer id,QueryCommentVo queryCommentVo,Integer page,Integer rows) {

        Map result = buildReturnMap(false, "");
        User user = userService.getUserById(id);
        if (null != user) {
            queryCommentVo.setUserId(id);
            gameService.listComments(queryCommentVo,page,rows);
        }
        return result;
    }
}
