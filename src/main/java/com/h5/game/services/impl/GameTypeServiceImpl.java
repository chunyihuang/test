package com.h5.game.services.impl;


import com.h5.game.model.bean.GameType;
import com.h5.game.dao.interfaces.GameTypeDao;
import com.h5.game.services.interfaces.GameTypeService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by 黄春怡 on 2017/4/10.
 */
@Service
public class GameTypeServiceImpl implements GameTypeService {

    private Logger logger = Logger.getLogger(this.getClass());
    @Autowired
    private GameTypeDao gameTypeDao;

    public Map saveOrUpdateUser(GameType gameType) {
        Map<String ,Object>result = new HashMap<String ,Object>();
        boolean isChange = false;
        try {
            GameType gt = null;
            if (gt.getId() != null) {
                isChange = true;//如果存在id,则表示为修改
                gt = (GameType) gameTypeDao.getById(gameType.getId());
                if (gt == null) {
                    result.put("status",false);
                    result.put("reason","对象不存在");
                    return result;
                }
            }
            gt.setName(gameType.getName());
            gt.setType(gameType.getType());

            gameTypeDao.saveOrUpdate(gt);
        } catch (Exception e) {
            logger.error("发生错误", e.getCause());
        }
        if (isChange == true) {
            result.put("status",true);
            result.put("reason","修改成功！");
            return result;

        } else {
            result.put("status",true);
            result.put("reason","添加！");
            return result;

        }
    }

    public List listAll() {
        return gameTypeDao.findAllList(GameType.class);
    }

    public Boolean removeAdmin(Integer id) {
         gameTypeDao.delete(gameTypeDao.getById(id));
         return true;
    }
}
