package com.h5.game.controller.wechat;

import com.h5.game.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by 黄春怡 on 2017/4/27.
 */
@Controller
@RequestMapping(value = "/wechat")
public class WeChatController extends BaseController{
    @RequestMapping(value = "/test",method = RequestMethod.GET)
    @ResponseBody
    public void test(HttpServletRequest request, HttpServletResponse response){
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        System.out.println("signature:" + signature);
        System.out.println("timestamp:" + timestamp);
        System.out.println("nonce:" + nonce);
        System.out.println("echostr:" + echostr);
        try {
            PrintWriter pw = response.getWriter();
            pw.append(echostr);
            pw.flush();
        }catch (IOException e){
            e.printStackTrace();
        }

    }

}
