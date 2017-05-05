package com.h5.game.dao.impl;

import com.h5.game.model.bean.Game;
import com.h5.game.dao.base.BaseDao;
import com.h5.game.dao.base.PageResults;
import com.h5.game.dao.interfaces.GameDao;
import com.h5.game.model.vo.QueryGameVo;
import org.hibernate.Criteria;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 黄春怡 on 2017/4/10.
 */
@Repository("gameDao")
public class GameDaoImpl extends BaseDao implements GameDao {
    {
        zclass = new Game();
    }
    public PageResults listGames(QueryGameVo queryGameVo, Integer page, Integer rows) {
        Criteria criteria = getSession().createCriteria(zclass.getClass());
        if(null != queryGameVo.getGameName()){
            criteria.add(Restrictions.like("gameName","%"+queryGameVo.getGameName()+"%"));
        }
        if(null != queryGameVo.getGameTagId()){
            criteria.add(Restrictions.eq("gameTag.id",queryGameVo.getGameTagId()));
        }
        if(null != queryGameVo.getGameTypeId()){
            criteria.add(Restrictions.eq("gameType.id",queryGameVo.getGameTypeId()));
        }
        if(null != queryGameVo.getUserId()){
            criteria.add(Restrictions.eq("user.id",queryGameVo.getUserId()));
        }
        if(null != queryGameVo.getCommended()){
            criteria.add(Restrictions.eq("commended",queryGameVo.getCommended()));
        }
        if(null != queryGameVo.getChecked()){
            criteria.add(Restrictions.eq("checked",queryGameVo.getChecked()));
        }
        if(null != queryGameVo.getUploadDate()){
            SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
            try {
                Date date = sdf.parse( queryGameVo.getUploadDate());
                System.out.println("传入的日期："+date.toString());
                criteria.add(Restrictions.between("uploadDate",date,new Date()));
            }catch (ParseException pe){
                pe.printStackTrace();
                return null;
            }


        }
        criteria.addOrder(Property.forName("scord").desc() );
        return pageByCriteria(criteria,page,rows);

    }
}
