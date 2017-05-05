package com.h5.game.services.interfaces;

import com.h5.game.model.bean.Admin;
import com.h5.game.model.bean.User;
import com.h5.game.services.cache.TokenCache;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by 黄春怡 on 2017/3/31.
 */
public interface AuthService {

    /******  AdminToken ***********/
    Admin getAdmin(HttpServletRequest request);

    Admin getAdmin(HttpSession session);

    void setAdmin(HttpServletRequest request, Admin admin);

    void setAdmin(HttpSession session, Admin admin);

    void saveTokenCache(String token, Admin admin);

    Admin getAmdinByToken(String token);

    /******  UserToken ***********/
    User getUser(HttpServletRequest request);

    User getUser(HttpSession session);

    void setUser(HttpServletRequest request, User user);

    void setUser(HttpSession session, User user);

    void saveTokenCache(String token, User user);

    User getUserByToken(String token);



    boolean updateTokenCache(String token);

    TokenCache getTokenCacheByToken(String token);


}
