package com.h5.game.dao.impl;

import com.h5.game.model.bean.Admin;
import com.h5.game.dao.base.BaseDao;
import com.h5.game.dao.base.PageResults;
import com.h5.game.dao.interfaces.AdminDao;
import com.h5.game.common.tools.db.model.KVParam;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 * Created by 黄春怡 on 2017/4/5.
 */
@Repository("adminDao")
public class AdminDaoImpl extends BaseDao implements AdminDao {
    {
        zclass = new Admin();
    }

    //根据用户名查找用户
    public Admin getByUserName(String userName){
        KVParam<String,Object> eqParam= new KVParam<String, Object>();
        eqParam.setKey("userName");
        eqParam.setValue(userName);
        return (Admin)this.findOneByCondition(eqParam,null,Admin.class);
    }

    public PageResults listAdmins(Integer page, Integer rows, String userName, String realName) {
        Criteria criteria = getSession().createCriteria(Admin.class);
        if(null != userName)
            criteria.add(Restrictions.eq("userName",userName));
        if(null != realName)

            criteria.add(Restrictions.like("realName",realName));
        return this.pageByCriteria(criteria,page,rows);
    }
}
