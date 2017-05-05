package com.h5.game.controller;


import com.h5.game.dao.base.PageResults;
import net.sf.json.JSONObject;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import java.util.HashMap;
import java.util.Map;

public class BaseController {

    protected ModelAndView getJsonModelAndView(Map<String, Object> map) {

        ModelAndView mav = new ModelAndView();
        MappingJackson2JsonView view = new MappingJackson2JsonView();
        view.setAttributesMap(map);
        mav.setView(view);

        return mav;
    }

    public String getJson(Map<String,Object> map){
        JSONObject object = JSONObject.fromObject(map);

        return object.toString();
    }

    public HashMap<String,Object> buildReturnMap(boolean status,String reason){
        HashMap<String,Object> map = new HashMap<String, Object>();
        map.put("status",status);
        map.put("reason",reason);
        return map;
    }

    protected Map<String,Object> checkPageParameters(Integer page,Integer rows){
        Map<String,Object> result = null;
        if(page == null || rows == null){
            result = buildReturnMap(false,"参数page和rows错误");
            return result;
        }
        if(page <= 0 || rows <= 0){
            result = buildReturnMap(false,"参数page和rows的值不能小于等于0");
            return result;
        }
        return result;
    }

    protected Map<String,Object> buildPageReturnMap(PageResults pageResults){
        Map<String,Object> result;
        if(pageResults == null){
            result = buildReturnMap(false,"发生未知错误,查询失败");
        }else{
            result = buildReturnMap(true,"查询成功");
            result.put("rows",pageResults.getData());
            result.put("count",pageResults.getTotalCount());
        }
        return result;
    }



}
