package com.h5.game.common.tools.db.support;

import com.h5.game.common.annotations.Ignore;
import com.h5.game.common.tools.BaseUtil;
import com.h5.game.common.tools.db.base.BaseVo;
import com.h5.game.common.tools.db.base.ConvertProcess;
import com.h5.game.common.tools.db.interfaces.HqlConvert;
import com.h5.game.common.tools.db.model.DateTimeEntity;
import com.h5.game.common.tools.db.model.KVParam;
import org.hibernate.criterion.MatchMode;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Component
public class SimpleHqlConvert extends ConvertProcess implements HqlConvert {

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public <T extends BaseVo> String voToHql(String beanName, T vo) {
        StringBuilder hqlBuffer = new StringBuilder();
        hqlBuffer.append(" FROM ").append(beanName).append(" as bean Where ");
        Class<?> voClass = vo.getClass();
        Field[] fields = voClass.getDeclaredFields();//获取所有属性
        HashMap<Integer, DateTimeEntity> dateTimeEntityHashMap = new HashMap<Integer, DateTimeEntity>();//存放时间比较Map
        //遍历每个属性
        for (Field field : fields) {
            Ignore ignore = field.getAnnotation(Ignore.class);
            if (ignore != null && ignore.isIgnore()) {    //判断是否标有忽略注解,如果有则跳过这个属性
                continue;
            }
            KVParam<String, Object> param = getEntry(vo, field);
            if (!isAdd(param.getValue())) continue; //不需要添加的时候跳过
            doDispatcher(param, field, dateTimeEntityHashMap,null, hqlBuffer);
        }
        String result = hqlBuffer.toString();
        if (result.endsWith(" Where ")) {
            result = result.replace(" Where ", "");
        } else if (result.endsWith(" AND ")) {
            result = result.substring(0, result.lastIndexOf(" AND "));
        }
        return result;
    }

    /**
     * create on 2016/8/12 <br>
     * author renjie <br>
     * version 1.0   <br>
     * 添加 = 对比到Criteria中
     */
    protected void addEq(KVParam<String, Object> param, Object adapterParam) {
        StringBuilder hqlBuffer = (StringBuilder) adapterParam;
        if (param.getValue() instanceof String) {
            hqlBuffer.append("bean.").append(param.getKey()).append(" = '").append(param.getValue()).append("' AND ");
        } else {
            hqlBuffer.append("bean.").append(param.getKey()).append(" = ").append(param.getValue()).append(" AND ");
        }
    }

    protected void addIn(KVParam<String, Object> param, Object adapterParam) {
        StringBuilder hqlBuffer = (StringBuilder) adapterParam;
        Object[] objs = (Object[]) param.getValue();
        StringBuilder childStr = new StringBuilder();
        childStr.append("bean.").append(param.getKey()).append(" IN ( ");
        for (int i = 0; i < objs.length; i++) {
            if (objs[i] instanceof String) {
                if (i == (objs.length - 1)) {
                    childStr.append(" '").append(objs[i].toString()).append("'");
                } else {
                    childStr.append(" '").append(objs[i].toString()).append("' , ");
                }

            } else {
                if (i == (objs.length - 1)) {
                    childStr.append(objs[i].toString()).append(" ");
                } else {
                    childStr.append(objs[i].toString()).append(" , ");
                }
            }
        }
        childStr.append(" ) AND ");
        hqlBuffer.append(childStr);
    }

    protected void addOr(KVParam<String, Object> param, HashMap<Integer, List<KVParam>> orParamModelHashMap, int i, Object adapterParam) {
        //TODO 等待完善
    }

    protected void addBetween(KVParam<String, Object> param, Field field, HashMap<Integer, DateTimeEntity> dateTimeEntityHashMap, String splitChart, Object adapterParam) {
        StringBuilder hqlBuffer = (StringBuilder) adapterParam;
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
                    hqlBuffer.append("bean.").append(param.getKey()).append(" BETWEEN '").append(dateFormat.format(date1)).append("' AND '").append(dateFormat.format(date2)).append("' AND ");
                } else {              //如果date2的时间大于date1
                    hqlBuffer.append("bean.").append(param.getKey()).append(" BETWEEN '").append(dateFormat.format(date2)).append("' AND '").append(dateFormat.format(date1)).append("' AND ");
                }
            } catch (ParseException e) {
                logger.error("发生转换异常", e.getCause());
            }
        }
    }

    /**
     * create on 2016/8/12 <br>
     * author renjie <br>
     * version 1.0   <br>
     * 添加 LIKE 对比到Criteria中
     *
     * @param mode 对比模式
     */
    protected void addLike(KVParam<String, Object> param, MatchMode mode, Object adapterParam) {
        StringBuilder hqlBuffer = (StringBuilder) adapterParam;
        if (param.getValue() instanceof String) { //只有string类型才可以使用like比较
            hqlBuffer.append("bean.").append(param.getKey()).append(" like ");//.append(param.getValue()).append(" AND ");
            switch (mode) {
                case EXACT:
                    hqlBuffer.append("'").append(param.getValue()).append("'");
                    break;
                case START:
                    hqlBuffer.append(" '%").append(param.getValue()).append("'");
                    break;
                case END:
                    hqlBuffer.append("'").append(param.getValue()).append("%' ");
                    break;
                case ANYWHERE:
                    hqlBuffer.append(" '%").append(param.getValue()).append("%' ");
                    break;
            }
            hqlBuffer.append(" AND ");
        }
    }

    protected void addGeOrGt(KVParam<String, Object> param, boolean isGe, Object adapterParam) {
        StringBuilder hqlBuffer = (StringBuilder) adapterParam;
        if (isGe) {
            hqlBuffer.append("bean.").append(param.getKey()).append(" >= ").append(param.getValue()).append(" AND ");
        } else {
            hqlBuffer.append("bean.").append(param.getKey()).append(" > ").append(param.getValue()).append(" AND ");
        }
    }

    protected void addLeOrLt(KVParam<String, Object> param, boolean isLe, Object adapterParam) {
        StringBuilder hqlBuffer = (StringBuilder) adapterParam;
        if (isLe) {
            hqlBuffer.append("bean.").append(param.getKey()).append(" <= ").append(param.getValue()).append(" AND ");
        } else {
            hqlBuffer.append("bean.").append(param.getKey()).append(" < ").append(param.getValue()).append(" AND ");
        }
    }
}
