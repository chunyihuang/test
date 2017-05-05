package com.h5.game.dao.interfaces;


import com.h5.game.dao.base.IBaseDao;
import com.h5.game.dao.base.PageResults;
import com.h5.game.model.bean.User;
import com.h5.game.model.vo.UserVo;


/**
 * Created by 黄春怡 on 2017/4/7.
 */
public interface UserDao extends IBaseDao {
    User getByUserName(String userName);

    PageResults pageUsers(String user, String email, String wechat, Integer page, Integer rows);
    PageResults pageUsers(UserVo userVo, Integer page, Integer rows);

}
