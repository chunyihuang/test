package com.h5.game.common.tools.db.interfaces;

import com.h5.game.common.tools.db.base.BaseVo;
import org.hibernate.Criteria;

/**
 * Created by 黄春怡 on 2017/4/1.
 */
public interface CriteriaConvert {

    /**
     * create on 2016/8/11 <br>
     * author renjie <br>
     * version 1.0   <br>
     * 把Vo的属性转换为Criteria里面的查询语句
     * @param criteria criteria对象
     * @param vo Vo类
     * @param <T> extends BaseVo
     * @return 返回添加完条件的Criteria类
     */
        <T extends BaseVo> Criteria voToCriteria(Criteria criteria, T vo);


}
