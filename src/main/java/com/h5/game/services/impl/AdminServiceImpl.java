package com.h5.game.services.impl;

import com.h5.game.common.constants.Constants;
import com.h5.game.dao.base.PageResults;
import com.h5.game.dao.interfaces.RoleDao;
import com.h5.game.dao.interfaces.SystemConfigDao;
import com.h5.game.model.bean.Admin;
import com.h5.game.dao.interfaces.AdminDao;
import com.h5.game.model.bean.Role;
import com.h5.game.model.bean.SystemConfig;
import com.h5.game.model.bean.User;
import com.h5.game.services.interfaces.AdminService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by 黄春怡 on 2017/3/31.
 */
@Service
public class AdminServiceImpl implements AdminService {
    private Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private SystemConfigDao systemConfigDao;

    @Autowired
    private AdminDao adminDao;

    @Autowired
    private RoleDao roleDao;
    public Admin getByUserName(String userName){
        return adminDao.getByUserName(userName);
    }

    public PageResults listAdmins(Integer page, Integer rows, String userName, String realName){
        return adminDao.listAdmins(page,rows,userName,realName);
    }

    public List listAll() {
       return adminDao.findAllList(Admin.class);
    }

    public Admin getAdminById(Integer id) {
       return (Admin) adminDao.getById(id);
    }

    public Boolean removeAdmin(Integer id) {
        if(null != id){
            Admin admin = (Admin)adminDao.getById(id);
            if(null != admin){
                adminDao.delete(admin);
                return true;
            }
        }
        return false;
    }

    public Map saveOrchangeAdmin(Admin admin,Integer roleId) {
        Map<String ,Object> result = new HashMap<String ,Object>();
        Admin a = null;
        boolean isChange = false;
        try {
            if (admin.getId() != null) {
                isChange = true;//如果存在id,则表示为修改
                a = (Admin) adminDao.getById(admin.getId());
                if (a == null) {
                    result.put("status",false);
                    result.put("reason","对象不存在");
                    return result;
                }
            }else {
                //判断用户名是否存在
                if(null != getByUserName(admin.getUserName())){
                    result.put("status",false);
                    result.put("reason","用户名已存在！");
                    return result;
                }
                a = new Admin();
            }
            Role role = (Role) roleDao.getById(roleId);
            if(null != role){
                result.put("status",false);
                result.put("reason","没有该类型的角色");
            }

            adminDao.saveOrUpdate(a);
        } catch (Exception e) {
            logger.error("发生错误", e.getCause());
            result.put("status",false);
            result.put("reason","操作失败！");
        }
        if (isChange == true) {
            result.put("status",true);
            result.put("obj",a);
            result.put("reason","修改成功！");
            return result;
        } else {
            result.put("status",true);
            result.put("obj",a);
            result.put("reason","添加成功！");
            return result;
        }
    }
    public List listConfig(){
        return systemConfigDao.findAllList(SystemConfig.class);
    }

    @Transactional
    public Map saveOrUpdateUser(SystemConfig systemConfig) {

        Map<String ,Object>result = new HashMap<String ,Object>();
        boolean isChange = false;
        try {
            if (systemConfig.getId() != null) {
                isChange = true;//如果存在id,则表示为修改
                if (systemConfigDao.getById(systemConfig.getId()) == null) {
                    result.put("status",false);
                    result.put("reason","对象不存在");
                    return result;
                }
            }
            systemConfigDao.saveOrUpdate(systemConfig);
        } catch (Exception e) {
            logger.error("发生错误", e.getCause());
            result.put("status",true);
            result.put("reason","操作失败！");
        }
        if (isChange == true) {
            result.put("status",true);
            result.put("obj",systemConfig);
            result.put("reason","修改成功！");
            return result;

        } else {
            result.put("status",true);
            result.put("obj",systemConfig);
            result.put("reason","添加成功！");
            return result;

        }
    }
}
