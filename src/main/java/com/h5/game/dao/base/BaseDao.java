package com.h5.game.dao.base;

import com.h5.game.common.tools.BaseUtil;
import com.h5.game.common.tools.db.base.BaseVo;
import com.h5.game.common.tools.db.interfaces.CriteriaConvert;
import com.h5.game.common.tools.db.model.KVParam;
import com.h5.game.model.base.BaseBean;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by 黄春怡 on 2017/4/1.
 */
public class BaseDao implements IBaseDao {

    @Autowired
    protected SessionFactory sessionFactory;
    protected Session session;
    protected BaseBean zclass;

    @Autowired
    private CriteriaConvert criteriaConvert;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Session getSession() {

        session = null;
        try {
            session = sessionFactory.getCurrentSession();
        }catch (Exception e){
            e.printStackTrace();
            session = null;
        }
        return session;

    }

    public void setSession(Session session) {
        this.session = session;
    }

    public BaseBean getZclass() {
        return zclass;
    }

    public void setZclass(BaseBean zclass) {
        this.zclass = zclass;
    }


    @Transactional
    public void save(BaseBean baseBean) {
        getSession().save(baseBean);
        getSession().flush();
    }


    @Transactional
    public void update(BaseBean baseBean) {
        getSession().update(baseBean);
        getSession().flush();
    }


    @Transactional
    public void saveOrUpdate(BaseBean baseBean) {
        getSession().saveOrUpdate(baseBean);
        getSession().flush();
    }


    public BaseBean getById(Integer id) {
        if (null == id)
            return null;
        return (BaseBean) getSession().get(zclass.getClass(),id);
    }


    public void delete(BaseBean bean) {
        getSession().delete(bean);
        getSession().flush();
    }


    public <T extends BaseBean> PageResults<T> pageByCriteria(Criteria criteria, Integer page, Integer rows) {
        if( null == page) page = 1;
        if( null == rows) rows = 10;
        PageResults<T> results = new PageResults<T>();
        int currentPage = page > 1 ? page:1;
        criteria.setFirstResult((currentPage - 1) * rows);//设置偏移数
        criteria.setMaxResults(rows);
        results.setCurrentPage(currentPage);
        results.setPageSize(rows);
        //results.resetPageNo();
        List list = criteria.list();
        if ( null != list ){
           results.setData(list);
        }
        criteria.setFirstResult(0);
        Object countRes = criteria.setProjection(Projections.rowCount()).uniqueResult();
        if(countRes == null) countRes = 0;
        results.setTotalCount(Integer.valueOf(countRes.toString()));
        return results;
    }



    public <T extends BaseBean,V extends BaseVo> PageResults<T> findPage(Integer page, Integer rows, V vo, String orderBy, String seq, Class<? extends BaseBean> beanClass) {

        if( null == page) page = 1;
        if( null == rows) rows = 10;
        Criteria criteria = getSession().createCriteria(beanClass.getClass());
        criteria = criteriaConvert.voToCriteria(criteria,vo);
        if(!BaseUtil.isEmpty(orderBy)){
            if("desc".equals(seq.trim().toLowerCase())){
                criteria.addOrder(Order.desc(orderBy));
            }else{
                criteria.addOrder(Order.asc(orderBy));
            }
        }
        return pageByCriteria(criteria,page,rows);
    }


    public <T extends BaseBean> List<T> findAllList(Class<T> baseBeanClass) {
        return getSession().createCriteria(baseBeanClass).list();
    }


    public Object findOneByCondition(KVParam<String ,Object> eqParam , KVParam<String ,Object> likeParam, Class<?> beanClass) {
        Criteria criteria = getSession().createCriteria(beanClass);
        if (null != eqParam){
            criteria.add(Restrictions.eq(eqParam.getKey(),eqParam.getValue())).addOrder(Order.asc("id")).setMaxResults(1);
        }
        if (null != likeParam){
            criteria.add(Restrictions.like(eqParam.getKey(),"%"+eqParam.getValue()+"%")).addOrder(Order.asc("id")).setMaxResults(1);

        }
        return criteria.uniqueResult();
    }


    public List findListByCondition(KVParam<String ,Object> eqParam ,KVParam<String ,Object> likeParam, Class<?> beanClass) {
        Criteria criteria = getSession().createCriteria(beanClass);
        if (null != eqParam){
            criteria.add(Restrictions.eq(eqParam.getKey(),eqParam.getValue())).addOrder(Order.asc("id")).setMaxResults(1);
        }
        if (null != likeParam){
            criteria.add(Restrictions.like(eqParam.getKey(),"%"+eqParam.getValue()+"%")).addOrder(Order.asc("id")).setMaxResults(1);
        }
        return criteria.list();
    }


    public <T extends BaseBean, K extends BaseVo> List<T> findAllListByConditionVo(K conditionVo, Class<T> beanClass) {
        Criteria criteria = getSession().createCriteria(beanClass);
        criteria = criteriaConvert.voToCriteria(criteria,conditionVo);
        return criteria.list();
    }


    public <T extends BaseBean, K extends BaseVo> PageResults<T> findPageByVoHql(Integer page, Integer rows, K vo, String orderBy, String seq, Class<T> beanClass) {
        return null;
    }
}
