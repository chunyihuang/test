package com.h5.game.dao.impl;

import com.h5.game.model.bean.Comment;
import com.h5.game.dao.base.BaseDao;
import com.h5.game.dao.base.PageResults;
import com.h5.game.dao.interfaces.CommentDao;
import com.h5.game.model.vo.QueryCommentVo;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 黄春怡 on 2017/4/13.
 */
@Repository("commentDao")
public class CommentDaoImpl extends BaseDao implements CommentDao {
    {
        zclass = new Comment();
    }

    public PageResults listComments(QueryCommentVo commentVo, Integer page, Integer rows) {

        Criteria criteria = getSession().createCriteria(zclass.getClass());

        if(null != commentVo.getUserId()){
            criteria.add(Restrictions.eq("user.id",commentVo.getUserId()));
        }
        if(null != commentVo.getGameId()){
            criteria.add(Restrictions.eq("game.id",commentVo.getGameId()));
        }
        if(null != commentVo.getDownload()){
            criteria.add(Restrictions.eq("download",commentVo.getDownload()));
        }
        if(null != commentVo.getLiked()){
            criteria.add(Restrictions.eq("liked",commentVo.getLiked()));
        }
        if(null != commentVo.getUploadDate()){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date = sdf.parse(commentVo.getUploadDate());
                criteria.add(Restrictions.between("uploadDate",date,new Date()));
            }catch (ParseException pe){
                pe.printStackTrace();
            }

        }
        return pageByCriteria(criteria,page,rows);


    }

    public Integer countComments(QueryCommentVo commentVo) {
        Criteria criteria = getSession().createCriteria(zclass.getClass());

        if(null != commentVo.getUserId()){
            criteria.add(Restrictions.eq("user.id",commentVo.getUserId()));
        }
        if(null != commentVo.getGameId()){
            criteria.add(Restrictions.eq("game.id",commentVo.getGameId()));
        }
        if(null != commentVo.getDownload()){
            criteria.add(Restrictions.eq("download",commentVo.getDownload()));
        }
        if(null != commentVo.getLiked()){
            criteria.add(Restrictions.eq("liked",commentVo.getLiked()));
        }
        if(null != commentVo.getUploadDate()){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date = sdf.parse(commentVo.getUploadDate());
                criteria.add(Restrictions.between("uploadDate",date,new Date()));
            }catch (ParseException pe){
                pe.printStackTrace();
            }

        }
       return criteria.list().size();
    }
}
