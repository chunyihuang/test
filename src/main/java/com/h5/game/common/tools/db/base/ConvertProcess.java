package com.h5.game.common.tools.db.base;

import com.h5.game.common.tools.BaseUtil;
import com.h5.game.common.tools.ReflectUtil;
import com.h5.game.common.tools.db.annotations.Operate;
import com.h5.game.common.tools.db.annotations.Rename;
import com.h5.game.common.tools.db.model.DateTimeEntity;
import com.h5.game.common.tools.db.model.KVParam;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

/**
 * 类用途 <br>
 * create on 2016/9/7
 *
 * @author 任杰
 * @version 1.0
 **/
public abstract class ConvertProcess extends OperateAdapter {

    protected Logger logger = Logger.getLogger(getClass());

    protected void doDispatcher(KVParam<String,Object> param, Field field, HashMap<Integer,DateTimeEntity> dateTimeEntityHashMap, HashMap<Integer,List<KVParam>> orParamModelHashMap, Object adapterParam){
        Operate op =  field.getAnnotation(Operate.class);
        if(op == null){
            addEq(param,adapterParam);     //添加eq判断
            return;
        }
        switch (op.value()){
            case EQ:                    //添加eq判断
                addEq(param,adapterParam);
                break;
            case LIKE:                  //添加like判断
                addLike(param,op.mode(),adapterParam);
                break;
            case BETWEEN:              //添加between判断
                addBetween(param,field,dateTimeEntityHashMap,op.splitChar(),adapterParam);
                break;
            case GE:
                addGeOrGt(param,true,adapterParam);
                break;
            case GT:
                addGeOrGt(param,false,adapterParam);
                break;
            case LE:
                addLeOrLt(param,true,adapterParam);
                break;
            case LT:
                addLeOrLt(param,false,adapterParam);
                break;
            case OR:
                addOr(param,orParamModelHashMap,op.groupId(),adapterParam);
                break;
            case IN:
                addIn(param,adapterParam);
                break;
        }
    }
    /**
     * create on 2016/8/12 <br>
     * author renjie <br>
     * version 1.0   <br>
     * 获取KVParam单个键值对类
     * @param field 属性
     * @return 返回KVParam类
     */
    protected KVParam<String,Object> getEntry(Object voBean, Field field){
        KVParam<String,Object> param = new KVParam<String, Object>();
        try{
            Rename name = field.getAnnotation(Rename.class);    //获取属性上的注解
            String key ;
            if(name == null){  //如果不存在Rename注解则直接使用字段名
                key = field.getName();
            }else{
                if(name.value().trim().length() <= 0){ //认为value没有值的时候
                    key = field.getName();
                }else{
                    key = name.value().trim();
                }
            }
            Object value = ReflectUtil.getFieldValue(voBean,field.getName());
            param.setKey(key);
            param.setValue(value);

            return param;
        } catch (NoSuchMethodException e) {
            logger.error(e.getMessage());
            return param;
        }

    }

    /**
     * create on 2016/8/12 <br>
     * author renjie <br>
     * version 1.0   <br>
     * 判断值是否为null 且是否可以添加
     * @param value 需要判断的值
     * @return true表示可以添加 false表示不可以
     */
    protected boolean isAdd(Object value) {
        if (value == null)
            return false;
        String simpleName = value.getClass().getSimpleName();
        //判断是否为int型
        if(int.class.isAssignableFrom(value.getClass()) || Integer.class.isAssignableFrom(value.getClass())){
            return Integer.valueOf(value.toString()) > 0;
        }
        if(String.class.isAssignableFrom(value.getClass())){
            return !BaseUtil.isEmpty(value.toString());
        }
        return true;
    }
}
