package com.h5.game.dao.base;

import com.h5.game.common.tools.db.base.BaseVo;
import com.h5.game.common.tools.db.model.KVParam;
import com.h5.game.model.base.BaseBean;
import org.hibernate.Criteria;

import java.util.List;


/**
 * 基础DAO类方法接口
 * Created by 黄春怡 on 2017/4/1.
 */
public interface IBaseDao {

    void save(BaseBean baseBean);
    void update(BaseBean baseBean);
    void saveOrUpdate(BaseBean baseBean);
    BaseBean getById(Integer id);
    void delete(BaseBean bean);
    /**
     * 使用Criteria进行分页查询
     * @param criteria criteria 条件
     * @param page 当前页
     * @param rows 每页条数
     * @param <T> extends BaseDto
     * @return 返回结果
     */
    <T extends BaseBean>PageResults<T> pageByCriteria(Criteria criteria, Integer page, Integer rows);

    <T extends BaseBean,V extends BaseVo> PageResults<T> findPage(Integer page, Integer rows, V vo, String orderBy, String seq, Class<? extends BaseBean> beanClass);

    <T extends BaseBean> List<T> findAllList(Class<T> baseBeanClass);

    Object findOneByCondition(KVParam<String, Object> eqParam, KVParam<String, Object> likeParam, Class<?> beanClass);

    List findListByCondition(KVParam<String, Object> eqParam, KVParam<String, Object> likeParam, Class<?> beanClass);

    <T extends BaseBean,K extends BaseVo> List<T> findAllListByConditionVo(K conditionVo, Class<T> beanClass);

    <T extends BaseBean, K extends BaseVo> PageResults<T> findPageByVoHql(Integer page, Integer rows, K vo, String orderBy, String seq, Class<T> beanClass);



}



