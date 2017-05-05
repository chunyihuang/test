package com.h5.game.dao.impl;


import com.h5.game.common.tools.db.model.KVParam;
import com.h5.game.dao.base.BaseDao;
import com.h5.game.dao.base.PageResults;
import com.h5.game.dao.interfaces.UserDao;

import com.h5.game.model.bean.User;
import com.h5.game.model.vo.UserVo;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;


/**
 * Created by 黄春怡 on 2017/4/7.
 */
@Repository("userDao")
public class UserDaoImpl extends BaseDao implements UserDao {
    {
        zclass = new User();
    }

    public User getByUserName(String userName) {

        Criteria criteria = getSession().createCriteria(User.class);
        criteria.add(Restrictions.eq("user",userName));
        User user = (User) criteria.uniqueResult();
        return user;
    }



    public PageResults pageUsers(UserVo userVo, Integer page, Integer rows) {
        return this.findPage(page,rows,userVo,null,null,User.class);
    }

    public PageResults pageUsers(String user, String email, String wechat, Integer page, Integer rows) {
        Criteria criteria = getSession().createCriteria(zclass.getClass());
        if(null != user ){
            criteria.add(Restrictions.like("user","%"+user+"%"));
        }
        if(null != email){
            criteria.add(Restrictions.like("email","%"+email+"%"));
        }
        if(null != wechat){
            criteria.add(Restrictions.like("wechat","%"+wechat+"%"));
        }
        return this.pageByCriteria(criteria,page,rows);
    }
}
