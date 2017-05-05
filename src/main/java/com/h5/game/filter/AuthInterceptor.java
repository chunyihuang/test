package com.h5.game.filter;

import com.h5.game.model.bean.Admin;
import com.h5.game.common.constants.Constants;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.regex.Pattern;

/**
 * Created by 黄春怡 on 2017/4/7.
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final Pattern regPattern = Pattern.compile("//S*.html");

    //拦截HTML的跳转
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {

         String url = httpServletRequest.getRequestURI().replace(httpServletRequest.getContextPath(),"");

         if(!url.contains("/login.html")){
             Admin admin = (Admin)httpServletRequest.getSession().getAttribute(Constants.SESSION_ADMIN);
             if (null == admin){
                 httpServletResponse.sendRedirect("/admin/login.html");
                 return false;
             }
         }
         return true;
    }

    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
