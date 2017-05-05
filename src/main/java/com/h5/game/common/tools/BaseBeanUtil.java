package com.h5.game.common.tools;

import com.h5.game.common.tools.convert.interfaces.CustomConvert;
import com.h5.game.common.tools.convert.interfaces.TransformConvert;
import com.h5.game.common.tools.convert.support.CustomConvertImpl;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 黄春怡 on 2017/4/1.
 */
public class BaseBeanUtil {

    private Logger logger = Logger.getLogger(getClass());

    private CustomConvert customConvert = new CustomConvertImpl();

    public void registerCustomConvert(Class<?> classType, TransformConvert convert){
        customConvert.registerCustomConvert(classType,convert);
    }

    /**
     * 把bean转换为Map对象形式
     */
    public Map<String,Object> transformBeanToMap(Object object,Class<?> eachClass){
        try {
            return transformBeanToMap(object,eachClass,null);
        } catch (Exception e) {
            logger.error("发生错误 : " + e );
            return null;
        }
    }

    /**
     * 把bean转换为Map对象形式
     */
    public Map<String,Object> transformBeanToMap(Object object,Class<?> eachClass,Class<?> initClass) throws IllegalAccessException, InstantiationException {
        Map<String,Object> result = new HashMap<String, Object>();
        if(object == null && initClass != null){
            object = initClass.newInstance();
        }
        if(object == null){
            return null;
        }
        Class<?> beanClass = object.getClass();
        Field[] fields = beanClass.getDeclaredFields();
        for (Field field : fields){
            String fileName = field.getName();
            Object value ;
            try {
                value = ReflectUtil.getFieldValue(object,field);
                if(eachClass != null && eachClass.isAssignableFrom(field.getType())){
                    result.put(fileName,transformBeanToMap(value,eachClass,field.getType()));
                }else{
                    TransformConvert convert = customConvert.findTransformConvert(field.getType());
                    if(convert != null){
                        value = convert.convert(fileName,value);
                    }
                    result.put(fileName,value);
                }
            } catch (IllegalAccessException e) {
                logger.error("取值错误 : " + e, e.fillInStackTrace());
            } catch (NoSuchMethodException e) {
                logger.error("没有发现get方法 : " + fileName);
            }
        }
        return result;
    }
}
