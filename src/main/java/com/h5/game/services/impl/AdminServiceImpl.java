package com.h5.game.services.impl;

import com.h5.game.dao.base.PageResults;
import com.h5.game.model.bean.Admin;
import com.h5.game.dao.interfaces.AdminDao;
import com.h5.game.services.interfaces.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by 黄春怡 on 2017/3/31.
 */
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminDao adminDao;

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


}
