package com.h5.game.dao.impl;


import com.h5.game.dao.base.BaseDao;
import com.h5.game.dao.base.PageResults;
import com.h5.game.dao.interfaces.SystemConfigDao;
import com.h5.game.dao.interfaces.UserDao;
import com.h5.game.model.bean.SystemConfig;
import com.h5.game.model.bean.User;
import com.h5.game.model.vo.UserVo;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;


/**
 * Created by 黄春怡 on 2017/4/7.
 */
@Repository("systemConfigDao")
public class SystemConfigDaoImpl extends BaseDao implements SystemConfigDao {
    {
        zclass = new SystemConfig();
    }


}
