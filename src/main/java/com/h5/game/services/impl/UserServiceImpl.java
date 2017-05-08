package com.h5.game.services.impl;

import com.h5.game.dao.base.PageResults;
import com.h5.game.model.bean.User;
import com.h5.game.common.constants.Constants;
import com.h5.game.dao.interfaces.UserDao;
import com.h5.game.services.interfaces.UserService;
import com.h5.game.model.vo.UserVo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by 黄春怡 on 2017/4/7.
 */
@Service
public class UserServiceImpl implements UserService {

    private Logger logger = Logger.getLogger(getClass());

    @Autowired
    private UserDao userDao;

    @Transactional
    public Map saveOrUpdateUser(UserVo userVo,HttpSession session){

        Map<String ,Object>result = new HashMap<String ,Object>();
        User u = null;
        boolean isChange = false;
        try {
            if (userVo.getId() != null) {
                isChange = true;//如果存在id,则表示为修改
                u = (User)userDao.getById(userVo.getId());
                if (u == null) {
                    result.put("status",false);
                    result.put("reason","对象不存在");
                    return result;
                }
            }else {
                //判断用户名是否存在
                if(null != getByUserName(userVo.getUser())){
                    result.put("status",false);
                    result.put("reason","用户名已存在！");
                    return result;
                }
                u = new User();
            }
            String uu= userVo.getUser();
            u.setUser(uu);
            u.setPassword(userVo.getPassword());
            u.setEmail(userVo.getEmail());
            u.setWechat(userVo.getWechat());
            //保存用户头像
            if(null != userVo.getAvatar() && !userVo.getAvatar().isEmpty()) {
                if (null != session.getAttribute(Constants.SESSION_USER)) {
                    String path = session.getServletContext().getRealPath("/WEB-INF/avater/");
                    File avatar = new File(path, userVo.getUser());
                    try {
                        userVo.getAvatar().transferTo(avatar);
                    } catch (Exception e) {
                        logger.error("发生错误", e.getCause());
                        result.put("status", false);
                        result.put("reason", "头像上传失败！");
                        return result;
                    }
                    int mark = avatar.getPath().lastIndexOf("\\");
                    u.setAvatar(avatar.getPath().substring(mark).replaceAll("\\\\","/"));
                }
            }else {
                u.setAvatar("/WEB-INF/avater/avater.jpg");
            }
            userDao.saveOrUpdate(u);
        } catch (Exception e) {
            logger.error("发生错误", e.getCause());
            result.put("status",true);
            result.put("reason","操作失败！");
        }
        if (isChange == true) {
            result.put("status",true);
            result.put("obj",u);
            result.put("reason","修改成功！");
            return result;

        } else {
            result.put("status",true);
            result.put("obj",u);
            result.put("reason","注册成功！");
            return result;

        }
    }

    public User getByUserName(String userName){
        return userDao.getByUserName(userName);
    }

    public User getUserById(Integer id) {
        return (User)userDao.getById(id);
    }

    public PageResults pageUsers(UserVo userVo, Integer page, Integer rows) {
        return userDao.pageUsers(userVo,page,rows);
    }

    public PageResults pageUsers(String user, String email, String wechat, Integer page, Integer rows) {

        return userDao.pageUsers(user,email,wechat,page,rows);

    }

    @Transactional
    public Map lockedUser(Integer id, Boolean locked) {
         Map result = new HashMap();
         User user = (User)userDao.getById(id);
         if(null != user){
             user.setLocked(locked);
             userDao.saveOrUpdate(user);
             result.put("status",true);
             return result;
         }else {
             result.put("status",false);
             result.put("reason","没有该用户的信息！");
             return result;
         }
    }
}
