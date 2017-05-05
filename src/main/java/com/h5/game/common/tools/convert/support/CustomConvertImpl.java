package com.h5.game.common.tools.convert.support;

import com.h5.game.common.tools.convert.interfaces.CustomConvert;
import com.h5.game.common.tools.convert.interfaces.TransformConvert;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 类用途 <br>
 * create on 2016/9/6
 *
 * @author 任杰
 * @version 1.0
 **/
@Component
public class CustomConvertImpl implements CustomConvert {

    private Set<RegisterClass> registerClass = new HashSet<RegisterClass>();

    private Map<Integer,TransformConvert> customConvert = new HashMap<Integer, TransformConvert>();

    public void registerCustomConvert(Class<?> classType, TransformConvert convert){
        for (RegisterClass regClass : registerClass){
            if(regClass.getBeanClass().isAssignableFrom(classType)){
                return;
            }
        }
        RegisterClass regClass = new RegisterClass();
        regClass.setId(registerClass.size() + 1);
        regClass.setBeanClass(classType);
        registerClass.add(regClass);
        customConvert.put(regClass.getId(),convert);
    }

    public TransformConvert findTransformConvert(Class<?> classType){
        int id = -1;
        for (RegisterClass regClass : registerClass){
            if(regClass.getBeanClass().isAssignableFrom(classType)
                    || regClass.getBeanClass().getName().toLowerCase().endsWith(classType.getName().toLowerCase())){
                id = regClass.getId();
                break;
            }
        }
        return customConvert.get(id);
    }

    private class RegisterClass{
        private int id = -1;
        private Class<?> beanClass;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Class<?> getBeanClass() {
            return beanClass;
        }

        public void setBeanClass(Class<?> beanClass) {
            this.beanClass = beanClass;
        }
    }
}
