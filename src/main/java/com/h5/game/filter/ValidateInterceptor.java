package com.h5.game.filter;

import com.h5.game.common.tools.validate.annotations.RequestValidate;
import com.h5.game.common.tools.validate.annotations.Validate;
import com.h5.game.common.tools.validate.interfaces.ValidateClass;
import com.h5.game.common.tools.validate.model.ValidateResult;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 黄春怡 on 2017/4/7.
 */
@Component
public class ValidateInterceptor implements MethodInterceptor,HandlerInterceptor {

    @Autowired
    private ValidateClass validateClass;

    private final String VALIDATE_LIST = "VALIDATE_LIST";

    private final String VALUE_VALIDATE_MAP = "VALUE_VALIDATE_MAP";

    public Object invoke(MethodInvocation methodInvocation) throws Throwable {

        System.out.print("进入数据校验拦截器！");
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        List<Integer> validateList = (List<Integer>) request.getAttribute(VALIDATE_LIST); //获取需要校验的参数数组下标列表
        Map<Integer,ValidateParamertBean> validateValueMap = (Map<Integer,ValidateParamertBean>)request.getAttribute(VALUE_VALIDATE_MAP);
        Object[] args = methodInvocation.getArguments();//获取所有请求参数
        //对对象校验列表进行遍历
        if(validateList != null)
            for (int index : validateList){
                //此处进行遍历校验
                ValidateResult result = validateClass.validate(args[index],request.getRequestURI());
                if(!result.isPass()){//如果校验不通过则返回错误提示
                    return buildReturn(methodInvocation.getMethod().getReturnType().getName(),result);
                }
            }
        //对值校验列表进行遍历
        if(validateValueMap != null)
            for (Map.Entry<Integer,ValidateParamertBean> entry : validateValueMap.entrySet()){
                ValidateResult result = validateClass.validate(args[entry.getKey()],entry.getValue().getValidate(),entry.getValue().getFieldName());
                if(!result.isPass()){//如果校验不通过则返回错误提示
                    return buildReturn(methodInvocation.getMethod().getReturnType().getName(),result);
                }
            }
        //完成后移除参数
        request.removeAttribute(VALUE_VALIDATE_MAP);
        request.removeAttribute(VALIDATE_LIST);
        return methodInvocation.proceed();
    }

    /**
     * create on 2016/9/6 <br>
     * author renjie <br>
     * version 1.0   <br>
     * 根据返回值类型构建不同的返回值
     */
    private Object buildReturn(String className,ValidateResult validateResult){
        Object result = null;
        if(Map.class.getName().equals(className)){
            HashMap<String,Object> temp = new HashMap<String, Object>();
            temp.put("status",false);
            temp.put("reason",validateResult.getFailReason());
            temp.put("failField",validateResult.getFailFieldName());
            result = temp;
        }else if(ModelAndView.class.getName().equals(className)){
            HashMap<String,Object> temp = new HashMap<String, Object>();
            temp.put("status",false);
            temp.put("reason",validateResult.getFailReason());
            temp.put("failField",validateResult.getFailFieldName());
            ModelAndView mav = new ModelAndView();
            MappingJackson2JsonView view = new MappingJackson2JsonView();
            view.setAttributesMap(temp);
            mav.setView(view);
            result = mav;
        }else if(String.class.getName().equals(className)){
            result = "校验失败,未能通过校验,失败原因 ; " + validateResult.getFailReason() + " 失败字段 : " + validateResult.getFailFieldName();
        }
        return result;
    }

    //在次方法中存入返回值类型
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(handler instanceof HandlerMethod){
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            List<Integer> validateList = new ArrayList<Integer>();
            Map<Integer,ValidateParamertBean> valueValidateMap = new HashMap<Integer, ValidateParamertBean>();
            for (MethodParameter parameter : handlerMethod.getMethodParameters()){ //获取参数列表,并且遍历
                String parameterName = parameter.getParameterName();
                int index = parameter.getParameterIndex();
                if(parameter.hasParameterAnnotation(Validate.class)) {//判断参数上是否有Validate注解,有则进行简单值校验
                    if (parameter.hasParameterAnnotation(RequestParam.class)) { //如果存在RequestParam注解,则去value值
                        String temp = parameter.getParameterAnnotation(RequestParam.class).value();
                        if (!"".equals(temp)) parameterName = temp;
                    }
                    valueValidateMap.put(index, new ValidateParamertBean(parameter.getParameterAnnotation(Validate.class), parameterName));
                }else if(parameter.hasParameterAnnotation(RequestValidate.class)){ //判断参数是否有 RequestValidate注解
                    validateList.add(index);
                }
            }
            request.setAttribute(VALIDATE_LIST,validateList); //存入需要校验的参数数组下标列表
            request.setAttribute(VALUE_VALIDATE_MAP,valueValidateMap);//存取需要进行简单值校验的Map
        }
        return true;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

    /**
     * 内部类,为了保存注解对象和参数名称使用
     */
    private class ValidateParamertBean{
        private Validate validate;
        private String fieldName;

        public Validate getValidate() {
            return validate;
        }

        public void setValidate(Validate validate) {
            this.validate = validate;
        }

        public String getFieldName() {
            return fieldName;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        public ValidateParamertBean(Validate validate, String fieldName) {
            this.validate = validate;
            this.fieldName = fieldName;
        }
    }
}
