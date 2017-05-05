package com.h5.game.services.interfaces;


import com.h5.game.dao.base.PageResults;
import com.h5.game.model.bean.Admin;

import java.util.List;

/**
 * Created by 黄春怡 on 2017/3/31.
 */

public interface AdminService {


    PageResults listAdmins(Integer page, Integer rows, String userName, String realName);

    List listAll();

    Boolean removeAdmin(Integer id);

    Admin getByUserName(String userName);

    Admin getAdminById(Integer id);


}
