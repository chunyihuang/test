package com.h5.game.dao.interfaces;

import com.h5.game.dao.base.IBaseDao;
import com.h5.game.dao.base.PageResults;
import com.h5.game.model.vo.QueryCommentVo;


/**
 * Created by 黄春怡 on 2017/4/13.
 */
public interface CommentDao extends IBaseDao {

    PageResults listComments(QueryCommentVo commentVo, Integer page, Integer rows);

    Integer countComments(QueryCommentVo commentVo);


}
