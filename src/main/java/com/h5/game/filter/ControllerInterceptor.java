package com.h5.game.filter;

import com.alibaba.fastjson.JSONArray;
import com.h5.game.model.bean.Admin;
import com.h5.game.services.interfaces.AuthService;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 黄春怡 on 2017/4/7.
 */
@Component
public class ControllerInterceptor implements MethodInterceptor {
    private final String TOKEN = "token";

    @Autowired
    private AuthService authService;
    //拦截方法
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String url = request.getRequestURI();
        Class returnClass = methodInvocation.getMethod().getReturnType();
        if(!url.contains("/login") && !url.contains("/checkTokenStatus")){
            String token = request.getParameter(TOKEN);
            Admin admin = null;
            //如果token为null,则使用session检查
            if (token == null){
                admin  = authService.getAdmin(request);
                if(admin == null) return buildReturn(returnClass,"请先登陆");
            }else{
                admin = authService.getAmdinByToken(token);
                if(admin == null){
                    //lockService.addIP(ip);
                    return buildReturn(returnClass,"toke错误");
                }else{
                    //登陆成功则刷新token缓存
                    authService.updateTokenCache(token);
                }
            }
        }
        return methodInvocation.proceed();
    }

    private Object buildReturn(Class returnClass,String reason){
        Object result = null;
        if(Map.class.isAssignableFrom(returnClass)){
            HashMap<String,Object> temp = new HashMap<String, Object>();
            temp.put("status",false);
            temp.put("reason",reason);
            result = temp;
        }else if(ModelAndView.class.isAssignableFrom(returnClass)){
            HashMap<String,Object> temp = new HashMap<String, Object>();
            temp.put("status",false);
            temp.put("reason",reason);
            ModelAndView mav = new ModelAndView();
            MappingJackson2JsonView view = new MappingJackson2JsonView();
            view.setAttributesMap(temp);
            mav.setView(view);
            result = mav;
        }else if(String.class.isAssignableFrom(returnClass)){
            result = reason;
        }else if(JSONArray.class.isAssignableFrom(returnClass)){
            JSONArray jsonArray = new JSONArray();
            jsonArray.add(reason);
            result = jsonArray;
        }
        return result;
    }
}
