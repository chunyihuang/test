package com.h5.game.common.tools.validate.support;

import com.h5.game.common.tools.BaseUtil;

import com.h5.game.common.tools.convert.interfaces.CustomConvert;
import com.h5.game.common.tools.convert.interfaces.TransformConvert;
import com.h5.game.common.tools.convert.support.BooleanConvert;
import com.h5.game.common.tools.convert.support.CustomConvertImpl;
import com.h5.game.common.tools.convert.support.DateConvert;
import com.h5.game.common.tools.convert.support.IntegerConvert;
import com.h5.game.common.tools.validate.annotations.Validate;
import com.h5.game.common.tools.validate.annotations.ValidateBody;
import com.h5.game.common.tools.validate.annotations.ValidateCondition;
import com.h5.game.common.tools.validate.exception.ValidateConditionParameterCountErrorException;
import com.h5.game.common.tools.validate.exception.ValidateTypeErrorException;
import com.h5.game.common.tools.validate.interfaces.ValidateClass;
import com.h5.game.common.tools.validate.model.ValidateResult;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.h5.game.common.tools.ReflectUtil.getFieldValue;


/**
 * Created by 黄春怡 on 2017/4/1.
 */
@Component
public class SimpleValidateClassUtil implements ValidateClass {

    private static CustomConvert customConvert = new CustomConvertImpl();
    static {
        //注册时间类型转换器
        customConvert.registerCustomConvert(Date.class,new DateConvert());
        //注册Boolean类型转换器
        customConvert.registerCustomConvert(Boolean.class,new BooleanConvert());
        //注册Integer类型属性转换器
        customConvert.registerCustomConvert(Integer.class,new IntegerConvert());
    }

    public ValidateResult validate(Object bean, String url) {
        ValidateResult result = new ValidateResult(true);
        if(bean == null) return result;
        Class<?> beanClass = bean.getClass();
        Field[] fields = beanClass.getDeclaredFields();
        for (Field field : fields){
            try {
                //如果有RequestValidate 则进行深度遍历校验
                ValidateBody validateBody = field.getAnnotation(ValidateBody.class);
                if(validateBody != null){
                    if(!matchUrlPath(validateBody.urlPath(),url)) continue; //判断是否符合requestUrl路径
                    String parentFieldName = field.getName();
                    if(!BaseUtil.isEmpty(validateBody.value())) parentFieldName = validateBody.value(); //看看是否有规定返回的子属性名称
                    Object value = getFieldValue(bean,field); //取属性值
                    if(value == null){
                        if(validateBody.NullPointPass()) continue; //null是否通过
                        return buildNullPointResult(field.getName());
                    }
                    if(List.class.isAssignableFrom(field.getType()) ||
                            Set.class.isAssignableFrom(field.getType())){ //判断是否为集合类型
                        Collection collection = (Collection) value;
                        int index = 0 ;
                        for (Object obj : collection){
                            ValidateResult childResult = validate(obj,url);
                            if(!childResult.isPass()) {
                                return buildChildResult(childResult,index,parentFieldName);
                            }
                            index = index + 1;
                        }
                    }else{ //判断是否为对象
                        ValidateResult childResult = validate(value,url);
                        if(!childResult.isPass()){
                            return buildChildResult(childResult,-1,parentFieldName);
                        }
                    }
                }else{
                    //如果标有 Validate 注解,则进行校验
                    Validate validate = field.getAnnotation(Validate.class);//获取validate注解
                    if(validate == null) continue; //如果不存在则跳过
                    if(!matchUrlPath(validate.urlPath(),url)) continue; //判断是否符合requestUrl路径
                    //进行分类校验
                    //插入判断是否进行校验注解
                    //conditionValidate 返回false表示不继续进行校验
                    try {
                        if (!conditionValidate(bean,field)) continue;
                    } catch (ValidateConditionParameterCountErrorException e) {
                        System.out.println(e.getMessage());
                        continue;
                    }
                    boolean validateResult;
                    Object value = getFieldValue(bean,field.getName());
                    validateResult = dispatcher(bean,value,field.getName(),validate);
                    if(!validateResult){ //validateResult 为 false ,表示验证不通过
                        result = buildFailResult(result,field.getName(),validate);
                        return result;
                    }
                }
            } catch (IllegalAccessException e) {
                return new ValidateResult(false,e.getMessage());
            } catch (NoSuchMethodException e) {
                return new ValidateResult(false,e.getMessage(),field.getName());
            } catch (InvocationTargetException e) {
                return new ValidateResult(false,e.getMessage());
            } catch (ValidateTypeErrorException e) {
                return new ValidateResult(false,"属性" + field.getName() + "不是String类型参数",field.getName());
            }
        }
        return result;
    }

    private boolean matchUrlPath(String[] urls, String url) {
        if(urls == null || urls.length == 0) return true; //如果为空则直接返回需要校验
        //如果urls的个数大于0 ,但是url为null,则表示不通过校验
        if(urls.length > 0 && url == null) return false;
        for (String sourUrl : urls){
            if(url.contains(sourUrl)) return true;
        }
        //如果不存在符合的则返回false
        return false;
    }

    /**
     * create on 2016/9/5 <br>
     * author renjie <br>
     * version 1.0   <br>
     */
    public ValidateResult validate(Object bean) {
        return this.validate(bean,null);
    }

    /**
     * 判断是否需要继续校验
     */
    private boolean conditionValidate(Object bean,Field field) throws ValidateConditionParameterCountErrorException {
        ValidateCondition condition = field.getAnnotation(ValidateCondition.class);
        if(condition != null){ //如果存在条件,则先进行条件比较,看是否符合条件
            boolean doContinue = false;
            String[] conditions = condition.condition();
            String[] compareValues = condition.compareValue();
            if(conditions.length != compareValues.length) throw new ValidateConditionParameterCountErrorException("Field :" + field + " annotations field condition parameters count : " + conditions.length + ", compareValue parameters count : " + compareValues.length + ", not match");
            for (int i = 0; i < conditions.length; i ++) {
                try {
                    Object object = getFieldValue(bean, conditions[i]);
                    if (!isDoValidate(object, compareValues[i])) {
                        doContinue = true;
                        break;
                    }
                } catch (Exception e) {
                    System.out.println("条件属性 : " + conditions[i] + "不存在get或者is方法,跳过此属性校验");
                    doContinue = true;
                    break;
                }
            }
            if(doContinue) return false;
        }
        return true;
    }

    private boolean isDoValidate(Object object, String compareValue) {
        //需要比较的属性的值为null的时候
        if(object == null) return false;
        //取到的值不为空 且 compareValue的值不为空
        if(!BaseUtil.isEmpty(object.toString()) && !BaseUtil.isEmpty(compareValue)){
            TransformConvert convert = customConvert.findTransformConvert(object.getClass());
            Object compare = compareValue;
            if(convert != null){
                compare = convert.convert("",compareValue);
            }
            if(!object.equals(compare)) return false;
        }else if( (BaseUtil.isEmpty(object.toString()) && !BaseUtil.isEmpty(compareValue) ) //其中一个为空 另外一个不是空
                || ( !BaseUtil.isEmpty(object.toString()) && BaseUtil.isEmpty(compareValue) )){
            return false;
        }
        return true;
    }

    public ValidateResult validate(Object value, Validate validate, String fileName) {
        ValidateResult result = new ValidateResult(true);
        if(validate == null) return  result;
        //进行分类校验
        try{
            boolean validateResult;
            validateResult = dispatcher(value,value,fileName,validate);
            if(!validateResult){ //validateResult 为 false ,表示验证不通过
                result = buildFailResult(result,fileName,validate);
                return result;
            }
        } catch (IllegalAccessException e) {
            return new ValidateResult(false,e.getMessage());
        } catch (NoSuchMethodException e) {
            return new ValidateResult(false,e.getMessage());
        } catch (InvocationTargetException e) {
            return new ValidateResult(false,e.getMessage());
        } catch (ValidateTypeErrorException e) {
            return new ValidateResult(false,"属性不是String类型参数");
        }
        return result;
    }

    private boolean dispatcher(Object bean,Object value,String fileName,Validate validate) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, ValidateTypeErrorException {
        boolean validateResult = true;
        switch (validate.type()) {
            case Required:
                validateResult = doRequiredCheck(value);
                break;
            case Length:
                validateResult = doLengthCheck(value,bean.getClass().getName(),fileName,validate.minLength(),validate.maxLength());
                break;
            case Custom:
                validateResult = doCustomCheck(value,bean.getClass().getName(),fileName,validate.regex());
                break;
        }
        return validateResult;
    }

/*    private boolean doCustomCheck(Object bean, Field field, String regex) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, ValidateTypeErrorException {
        Object value = getFieldValue(bean, field);
        return doCustomCheck(value,regex);
    }*/

    private boolean doCustomCheck(Object value,String classPath,String fieldName,String regex) throws ValidateTypeErrorException {
        //如果为null 则直接返回校验失败
        if (value == null) return false;
        if(value instanceof String){
            String sValue = value.toString();
            //使用正则表达式进行验证
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(sValue);
            return matcher.find();
        }else{
            throw new ValidateTypeErrorException(classPath + "." + fieldName + " type error : is not the java.lang.String");
        }

    }

    /**
     * 进行长度校验,此校验只针对字符串进行
     */
/*    private boolean doLengthCheck(Object bean, Field field, int minLength, int maxLength) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, ValidateTypeErrorException {
        Object value = getFieldValue(bean, field);
        return doLengthCheck(value,bean.getClass().getName(),field.getName(),minLength,maxLength);
    }*/
    private boolean doLengthCheck(Object value,String classPath,String fieldName,int minLength,int maxLength) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, ValidateTypeErrorException {
        //如果为null 则直接返回校验失败
        if(value == null) return false;
        if(value instanceof String){
            String sValue = value.toString();
            //不去除左右两侧空格
            //如果长度小于最小长度 或者 长度大于最大长度 则校验不通过
            if(sValue.length() < minLength || sValue.length() > maxLength) return false;
        }else{
            throw new ValidateTypeErrorException(classPath + "." + fieldName + " type error : is not the java.lang.String");
        }
        return true;
    }

    /**
     * 进行非空校验
     */
    private boolean doRequiredCheck(Object value) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        //检测是否为null 是null则校验不通过
        if (value == null) return false;
        //检测String对象 ,检测String对象是否为空字符串,是则不通过
        return !(value instanceof String) || !BaseUtil.isEmpty(value.toString());
    }

    /**
     * 构建失败返回结果
     */
    private ValidateResult buildFailResult(ValidateResult result, String fieldName, Validate validate){

        if(!BaseUtil.isEmpty(validate.name()))
            fieldName = validate.name();
        result.setPass(false);
        result.setFailFieldName(fieldName);
        String reason ;
        switch (validate.type()) {
            case Required:
                reason = String.format("参数 %s 不能为空",fieldName);
                if(!BaseUtil.isEmpty(validate.reason())) reason = validate.reason();
                result.setFailReason(reason);
                break;
            case Length:
                reason = String.format("参数 %s 输入长度不正确,min : %s,max : %s",fieldName,validate.minLength(),validate.maxLength());
                if(!BaseUtil.isEmpty(validate.reason())) reason = validate.reason();
                result.setFailReason(reason);
                break;
            case Custom:
                reason = String.format("参数 %s 输入校验失败,自定义校验规则 : %s 不能通过",fieldName,validate.regex());
                if(!BaseUtil.isEmpty(validate.reason())) reason = validate.reason();
                result.setFailReason(reason);
                break;
        }
        return result;
    }

    private ValidateResult buildChildResult(ValidateResult validateResult, int index, String fileName){
        String tempName = validateResult.getFailFieldName();
        if(index != -1){
            validateResult.setFailFieldName(fileName + "[" + index + "]." + validateResult.getFailFieldName());
        }else{
            validateResult.setFailFieldName(fileName + "." + validateResult.getFailFieldName());
        }
        if(tempName != null){
            validateResult.setFailReason(validateResult.getFailReason().replace(tempName,validateResult.getFailFieldName()));
        }
        return validateResult;
    }

    private ValidateResult buildNullPointResult(String fileName){
        return new ValidateResult(false,"属性:" + fileName + "为null",fileName);
    }
}
