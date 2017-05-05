package com.h5.game.common.tools.db.support;

import com.h5.game.common.annotations.Ignore;
import com.h5.game.common.tools.BaseUtil;
import com.h5.game.common.tools.db.annotations.DateTime;
import com.h5.game.common.tools.db.base.BaseVo;
import com.h5.game.common.tools.db.base.ConvertProcess;
import com.h5.game.common.tools.db.interfaces.CriteriaConvert;
import com.h5.game.common.tools.db.model.DateTimeEntity;
import com.h5.game.common.tools.db.model.KVParam;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.*;


@Component("criteriaConvert")
public class SimpleCriteriaConvert extends ConvertProcess implements CriteriaConvert {
    private Logger logger = Logger.getLogger(getClass());

    public <T extends BaseVo> Criteria voToCriteria(Criteria criteria, T vo) {
        Class<?> voClass = vo.getClass();
        Field[] fields = voClass.getDeclaredFields();//获取所有属性
        List<String> aliasList = new ArrayList<String>(); //存放别名数组
        HashMap<Integer, DateTimeEntity> dateTimeEntityHashMap = new HashMap<Integer, DateTimeEntity>();//存放时间比较Map
        HashMap<Integer,List<KVParam>> orParamMap = new HashMap<Integer, List<KVParam>>();
        //遍历每个属性
        for (Field field : fields) {
            Ignore ignore = field.getAnnotation(Ignore.class);
            if (ignore != null && ignore.isIgnore()) {    //判断是否标有忽略注解,如果有则跳过这个属性
                continue;
            }
            KVParam<String, Object> param = getEntry(vo, field);
            if (!isAdd(param.getValue())) continue; //不需要添加的时候跳过
            addAliasList(param.getKey(), aliasList);
            doDispatcher(param, field, dateTimeEntityHashMap,orParamMap, criteria);
        }
        //遍历完成后对HashMap的数据进行处理
        convertMapDateToCriteria(criteria, dateTimeEntityHashMap);
        //进行or条件处理
        convertOrParamToCriteria(criteria,orParamMap);
        //起别名
        createAliasListToCriteria(criteria, aliasList);
        return criteria;
    }

    /**
     * 添加or条件到criteria中
     */
    private void convertOrParamToCriteria(Criteria criteria, HashMap<Integer, List<KVParam>> orParamMap) {
        for (Map.Entry<Integer,List<KVParam>> entry : orParamMap.entrySet()){
            Disjunction disjunction = Restrictions.disjunction();
            for (KVParam param : entry.getValue()){
                disjunction.add(Restrictions.eq(param.getKey().toString(),param.getValue()));
            }
            criteria.add(disjunction);
        }
    }

    private void createAliasListToCriteria(Criteria criteria, List<String> aliasList) {
        for (String alias : aliasList) {
            criteria.createAlias(alias, alias);
        }
    }

    private void addAliasList(String key, List<String> aliasList) {
        String[] aliasArr = key.split("\\.");
        if (aliasArr.length > 1) {
            String aliasStr = "";
            for (int i = 0; i < aliasArr.length - 1; i++) {
                aliasStr += aliasArr[i];
                if (!aliasList.contains(aliasStr)) aliasList.add(aliasStr);
                aliasStr += ".";
            }
        }
    }

    /**
     * create on 2016/8/12 <br>
     * author renjie <br>
     * version 1.0   <br>
     * 添加 = 对比到Criteria中
     */
    protected void addEq(KVParam<String, Object> param, Object adapterParam) {
        Criteria criteria = (Criteria) adapterParam;
        criteria.add(Restrictions.eq(param.getKey(), param.getValue()));
    }

    protected void addIn(KVParam<String, Object> param, Object adapterParam) {
        Criteria criteria = (Criteria) adapterParam;
        Object[] objs = (Object[]) param.getValue();
        criteria.add(Restrictions.in(param.getKey(), objs));
    }

    protected void addOr(KVParam<String, Object> param, HashMap<Integer, List<KVParam>> orParamModelHashMap, int groupId, Object adapterParam) {
        if(!orParamModelHashMap.containsKey(groupId)) orParamModelHashMap.put(groupId,new ArrayList<KVParam>());//不存在这个分组的时候进行初始化
        orParamModelHashMap.get(groupId).add(param);
    }

    /**
     * create on 2016/8/12 <br>
     * author renjie <br>
     * version 1.0   <br>
     * 添加 LIKE 对比到Criteria中
     */
    protected void addLike(KVParam<String, Object> param, MatchMode mode, Object adapterParam) {
        Criteria criteria = (Criteria) adapterParam;
        if (param.getValue() instanceof String) { //只有string类型才可以使用like比较
            criteria.add(Restrictions.like(param.getKey(), param.getValue().toString(), mode));
        }
    }

    /**
     * between比较,字符串形式适合数字日期等,
     */
    protected void addBetween(KVParam<String, Object> param, Field field, HashMap<Integer, DateTimeEntity> dateTimeEntityHashMap, String splitChart, Object adapterParam) {
        Criteria criteria = (Criteria) adapterParam;
        if (param.getValue() instanceof String) { //判断是否为String类型的值
            String value = param.getValue().toString();
            if (splitChart == null || "".equals(splitChart)) {    //判断分割字符是否可用
                return;
            }
            String[] sDates = value.split(splitChart);  //分割时间字符串
            try {
                Date date1 = BaseUtil.parseStringToDate(sDates[0]);
                Date date2;
                if (sDates.length < 2) {
                    date2 = new Date();
                } else {
                    date2 = BaseUtil.parseStringToDate(sDates[1]);
                }
                if (date1.before(date2)) {    //如果date1的时间小于date2
                    criteria.add(Restrictions.between(param.getKey(), date1, date2));
                } else {              //如果date2的时间大于date1
                    criteria.add(Restrictions.between(param.getKey(), date2, date1));
                }
            } catch (ParseException e) {
                logger.error("发生转换异常", e.getCause());
            }
        } else if (param.getValue() instanceof Date) {
            DateTime dateTime = field.getAnnotation(DateTime.class);
            if (dateTime != null) { //判断是否存在DateTime注解
                DateTimeEntity dateTimeEntity = dateTimeEntityHashMap.get(dateTime.id());
                if (dateTimeEntity == null) {
                    dateTimeEntity = new DateTimeEntity();
                }
                dateTimeEntity.setFieldName(param.getKey());
                switch (dateTime.type()) {
                    case START:
                        dateTimeEntity.setStartDate((Date) param.getValue());
                        break;
                    case END:
                        dateTimeEntity.setEndDate((Date) param.getValue());
                        break;
                }
                if (dateTimeEntity.isFull()) {
                    criteria.add(Restrictions.between(dateTimeEntity.getFieldName(), dateTimeEntity.getStartDate(), dateTimeEntity.getEndDate()));
                    dateTimeEntityHashMap.remove(dateTime.id());
                }
            }
        }
    }


    protected void addGeOrGt(KVParam<String, Object> param, boolean isGe, Object adapterParam) {
        Criteria criteria = (Criteria) adapterParam;
        if (isGe) {
            criteria.add(Restrictions.ge(param.getKey(), param.getValue()));
        } else {
            criteria.add(Restrictions.gt(param.getKey(), param.getValue()));
        }
    }

    protected void addLeOrLt(KVParam<String, Object> param, boolean isLe, Object adapterParam) {
        Criteria criteria = (Criteria) adapterParam;
        if (isLe) {
            criteria.add(Restrictions.le(param.getKey(), param.getValue()));
        } else {
            criteria.add(Restrictions.lt(param.getKey(), param.getValue()));
        }
    }

    private void convertMapDateToCriteria(Criteria criteria, HashMap<Integer, DateTimeEntity> dateTimeEntityHashMap) {
        Set<Map.Entry<Integer, DateTimeEntity>> entrys = dateTimeEntityHashMap.entrySet();
        for (Map.Entry<Integer, DateTimeEntity> entry : entrys) {
            DateTimeEntity entity = entry.getValue();
            if (!BaseUtil.isEmpty(entity.getFieldName())) {
                if (entity.getStartDate() != null) {
                    criteria.add(Restrictions.ge(entity.getFieldName(), entity.getStartDate()));
                } else if (entity.getEndDate() != null) {
                    criteria.add(Restrictions.le(entity.getFieldName(), entity.getStartDate()));
                }
            }
        }
    }
}
