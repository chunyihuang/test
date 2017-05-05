package com.h5.game.common.tools;

import com.esotericsoftware.reflectasm.MethodAccess;
import org.apache.commons.collections.FastHashMap;

import java.lang.reflect.Field;

/**
 * Created by 黄春怡 on 2017/4/1.
 */
public class ReflectUtil {

    private static FastHashMap modelAccessCache = new FastHashMap(16);
    static {
        modelAccessCache.setFast(true);
    }

    private static MethodAccess getMethodAccess(Class<?> beanClass){
        MethodAccess methodAccess = null;
        if(!modelAccessCache.containsKey(beanClass.getName())){
            methodAccess = MethodAccess.get(beanClass);
            modelAccessCache.put(beanClass.getName(),methodAccess);
        }else{
            methodAccess = (MethodAccess) modelAccessCache.get(beanClass.getName());
        }
        return methodAccess;
    }

    public static Object getFieldValueByMethodName(Object object,String methodName) throws NoSuchMethodException {
        MethodAccess methodAccess = getMethodAccess(object.getClass());
        int index = methodAccess.getIndex(methodName);
        if(index == -1) {
            throw new NoSuchMethodException("can't not found the field : " + methodName + " get or is method");
        }
        return methodAccess.invoke(object,index);
    }

    public static Object getFieldValue(Object obj,String fieldName)throws NoSuchMethodException{
        MethodAccess methodAccess = getMethodAccess(obj.getClass());
        int index = tryGetGetMethod(methodAccess,fieldName);  //尝试获取get方法
        if(index == -1){
            index = tryGetIsMethod(methodAccess,fieldName); //尝试获取is方法
        }
        if(index == -1) {
            throw new NoSuchMethodException("can't not found the field : " + fieldName + " get or is method");
        }
        return methodAccess.invoke(obj,index);
    }

    public static Object getFieldValue(Object obj,Field field) throws NoSuchMethodException{
       return getFieldValue(obj,field.getName());
    }

    public static boolean setFieldValueByMethodName(Object object,String methodName/*,Class<?> paramType,*/, Object paramValue) throws NoSuchMethodException {
        MethodAccess methodAccess = getMethodAccess(object.getClass());
        int index = -1;
        index = methodAccess.getIndex(methodName,1);
        if(index == -1) {
            throw new NoSuchMethodException("can't not found the field : " + methodName + " get or is method");
        }
        return doInvokeSetMethod(methodAccess,object,index,paramValue);
    }

    private static boolean doInvokeSetMethod(MethodAccess methodAccess, Object object, int index, Object... objects){
        try {
            methodAccess.invoke(object,index,objects);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static boolean setFieldValue(Object obj,String fileName,String fileType,Object setValue) throws NoSuchMethodException,IllegalAccessException{
        MethodAccess methodAccess = getMethodAccess(obj.getClass());
        int index = tryGetSetMethod(methodAccess,fileName);
        if(index == -1){
            throw new NoSuchMethodException("can't not found the field : " + fileName + " Set method");
        }
        //setValue 不为null 且 field的Type 和 setValue 的Type不同的时候抛出异常
        if(setValue != null && !setValue.getClass().getName().toLowerCase().endsWith(fileType.toLowerCase())){
            throw new IllegalAccessException("The field type is : " + fileType + " not match the value type : " + setValue.getClass().getName());
        }
        return doInvokeSetMethod(methodAccess,obj,index,setValue);
    }

    public static boolean setFieldValue(Object obj,Field field,Object setValue) throws NoSuchMethodException,IllegalAccessException{
        return setFieldValue(obj,field.getName(),field.getType().getName(),setValue);
    }

    private static int tryGetIsMethod(MethodAccess methodAccess, String fieldName){
        try {
            String methodName = "is" + toUpperCaseFirstOne(fieldName);
            return methodAccess.getIndex(methodName);
        }catch (Exception e){
            return -1;
        }
    }

    private static int tryGetGetMethod(MethodAccess methodAccess, String fieldName){
        try {
            String methodName = "get" + toUpperCaseFirstOne(fieldName);
            return methodAccess.getIndex(methodName);
        }catch (Exception e){
            return -1;
        }
    }

    private static int tryGetSetMethod(MethodAccess methodAccess, String fieldName){
        try {
            String methodName = "set" + toUpperCaseFirstOne(fieldName);
            return methodAccess.getIndex(methodName,1);
        }catch (Exception e){
            return -1;
        }
    }

    private static String toUpperCaseFirstOne(String str){
        return str.toUpperCase().charAt(0) + str.substring(1);
    }
}
