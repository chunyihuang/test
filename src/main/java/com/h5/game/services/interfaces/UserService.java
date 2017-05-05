package com.h5.game.services.interfaces;


import com.h5.game.dao.base.PageResults;
import com.h5.game.model.bean.User;
import com.h5.game.model.vo.UserVo;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Created by 黄春怡 on 2017/4/7.
 */
public interface UserService {

    Map saveOrUpdateUser(UserVo userVo, HttpSession session);

    User getByUserName(String userName);

    User getUserById(Integer id);

    PageResults pageUsers(UserVo userVo, Integer page, Integer rows);

    PageResults pageUsers(String user, String email, String wechat, Integer page, Integer rows);

    Map lockedUser(Integer id, Boolean locked);
}
