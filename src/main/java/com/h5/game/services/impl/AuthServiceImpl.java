package com.h5.game.services.impl;

import com.h5.game.model.bean.Admin;
import com.h5.game.model.bean.User;
import com.h5.game.common.constants.Constants;
import com.h5.game.services.cache.TokenCache;
import com.h5.game.services.interfaces.AuthService;
import org.apache.commons.collections.FastHashMap;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Calendar;
import java.util.Date;

/**
 * 通过session 获取Admin对象
 * Created by 黄春怡 on 2017/3/31.
 */
@Service
public class AuthServiceImpl implements AuthService {

    private final static FastHashMap tokenCacheMap = new FastHashMap();
    {
        tokenCacheMap.setFast(true);
    }

    private TokenCache getTokenCache(String token){
        return (TokenCache)tokenCacheMap.get(token);
    }

    private TokenCache getTokenCacheByNoOutDate(String token){
        TokenCache tokenCache = getTokenCache(token);
        if(tokenCache == null) return  null;
        if(tokenCache.getOutTime().before(new Date())){
            tokenCacheMap.remove(token);
            return null;
        }else{
            return tokenCache;
        }
    }

    private void saveTokenCache(String token,TokenCache tokenCache){
        tokenCacheMap.put(token,tokenCache);
    }

    private Date getOutTime(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR,10);
        return calendar.getTime();
    }


    public Admin getAdmin(HttpServletRequest request) {
       return getAdmin(request.getSession());
    }


    public Admin getAdmin(HttpSession session) {
         return (Admin)session.getAttribute(Constants.SESSION_ADMIN);
    }


    public void setAdmin(HttpServletRequest request, Admin admin) {
        setAdmin(request.getSession(),admin);
    }


    public void setAdmin(HttpSession session, Admin admin) {

        session.setAttribute(Constants.SESSION_ADMIN,admin);
    }


    public void saveTokenCache(String token, Admin admin) {
        TokenCache tokenCache = new TokenCache();
        tokenCache.setToken(token);
        tokenCache.setAdmin(admin);
        tokenCache.setOutTime(getOutTime());
        saveTokenCache(token,tokenCache);
    }


    public boolean updateTokenCache(String token) {
       getTokenCache(token).setOutTime(getOutTime());
       return true;
    }


    public TokenCache getTokenCacheByToken(String token) {
        return getTokenCacheByNoOutDate(token);
    }


    public Admin getAmdinByToken(String token) {
        TokenCache tokenCache = getTokenCacheByToken(token);
        if(tokenCache != null){
            return tokenCache.getAdmin();
        }
        return null;
    }


    public User getUser(HttpServletRequest request){
        return getUser(request.getSession());
    }

    public User getUser(HttpSession session){
        return (User)session.getAttribute(Constants.SESSION_USER);
    }

    public void setUser(HttpServletRequest request, User user){
        setUser(request.getSession(),user);
    }

    public void setUser(HttpSession session, User user){
        session.setAttribute(Constants.SESSION_USER,user);
    }

    public void saveTokenCache(String token, User user){
        TokenCache tokenCache = new TokenCache();
        tokenCache.setToken(token);
        tokenCache.setUser(user);
        tokenCache.setOutTime(getOutTime());
        saveTokenCache(token,tokenCache);
    }

    public User getUserByToken(String token){
        TokenCache tokenCache = getTokenCacheByToken(token);
        if(tokenCache != null){
            return tokenCache.getUser();
        }
        return null;
    }
}
