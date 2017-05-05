package com.h5.game.controller.wechat;



import com.h5.game.util.MenuUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/menu")
public class MenuController {
	/**
	 * 创建菜单
	 * */
	@RequestMapping("/createMenu")
	public void createMenu(){
		int status = MenuUtil.createMenu(MenuUtil.getMenu());
		if(status==0){
			System.out.println("菜单创建成功！");
		}else{
			System.out.println("菜单创建失败！");
		}
	}
	
}
