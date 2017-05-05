package com.h5.game.dao.interfaces;


import com.h5.game.dao.base.IBaseDao;
import com.h5.game.dao.base.PageResults;
import com.h5.game.model.bean.Admin;

/**
 * Created by 黄春怡 on 2017/4/5.
 */
public interface AdminDao extends IBaseDao {
    Admin getByUserName(String userName);
    PageResults listAdmins(Integer page, Integer rows, String userName, String password);
}