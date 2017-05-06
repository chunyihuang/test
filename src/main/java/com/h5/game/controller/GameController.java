package com.h5.game.controller;


import com.h5.game.common.tools.validate.annotations.Validate;
import com.h5.game.dao.base.PageResults;
import com.h5.game.model.bean.Game;
import com.h5.game.services.interfaces.AuthService;
import com.h5.game.services.interfaces.GameService;
import com.h5.game.services.interfaces.GameTypeService;

import com.h5.game.model.vo.QueryCommentVo;
import com.h5.game.model.vo.QueryGameVo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;
import java.util.Map;


/**
 * Created by 黄春怡 on 2017/4/9.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping(value = "/game")
public class GameController extends BaseController {

    @Autowired
    private GameService gameService;
    @Autowired
    private AuthService authService;
    @Autowired
    private GameTypeService gameTypeService;


    @RequestMapping(value = "/listGameType",method = RequestMethod.GET)
    @ResponseBody
    public List listGameType(){
        return gameTypeService.listAll();
    }

    @RequestMapping(value = "/download",method = RequestMethod.GET)
    @ResponseBody
    public Map downloadGame(@Validate Integer id) {
        return null;
    }


    @RequestMapping(value = "/getGame",method = RequestMethod.GET)
    @ResponseBody
    public Map getGame(@Validate Integer id) {
        Map result = null;
        Game game = gameService.getGameById(id);
        if(null != game){
             result = buildReturnMap(true,"");
             result.put("obj",game);

        }else{
            result = buildReturnMap(true,"没有查询到任何数据！");
        }
        return result;
    }

    @RequestMapping(value = "/listComments",method = RequestMethod.GET)
    @ResponseBody
    public Map listComments(QueryCommentVo commentVo,Integer page,Integer rows) {
        PageResults pageResults = gameService.listComments(commentVo,page,rows);
        if(null != pageResults && pageResults.getTotalCount()>0){
            Map map = buildReturnMap(true,"");
            map.put("count",pageResults.getTotalCount());
            map.put("list",pageResults.getData());
            return map;
        }else {
            return buildReturnMap(false,"没有找到任何数据");
        }
    }


    @RequestMapping(value = "/listGames",method = RequestMethod.GET)
    @ResponseBody
    public Map listGames(QueryGameVo queryGameVo,Integer page, Integer rows) {
        Map result = null;
        PageResults pageResults = gameService.listGames(queryGameVo,page,rows);
        if(null != pageResults && pageResults.getTotalCount() > 0){
            result = buildReturnMap(true,"");
            result.put("count",pageResults.getTotalCount());
            result.put("list",pageResults.getData());
        }else{
            result = buildReturnMap(true,"没有查询到任何数据！");
        }
        return result;
    }

    @RequestMapping(value = "/testApk",method = RequestMethod.POST)
    @ResponseBody
    public void testApk(String apkName,String srcPath){
            System.out.println("ddd");
           try {
                gameService.apk("chunyi","E:\\HTML5\\HTML5三国杀版连连看");
            }catch (IOException e){
                e.printStackTrace();
            }catch (InterruptedException e){
                e.printStackTrace();
            }


         System.out.println("over");



    }

    @RequestMapping(value = "/testConfig",method = RequestMethod.POST)
    @ResponseBody
    public void testConfig(String apkName,String srcPath){
        System.out.println("ddd");
        try {
            gameService.apk("chunyi","E:\\HTML5\\HTML5三国杀版连连看");
        }catch (IOException e){
            e.printStackTrace();
        }catch (InterruptedException e){
            e.printStackTrace();
        }


        System.out.println("over");



    }


}
