package com.h5.game.dao.impl;

import com.h5.game.dao.base.BaseDao;
import com.h5.game.dao.interfaces.GameTypeDao;
import com.h5.game.model.bean.GameType;
import org.springframework.stereotype.Repository;

/**
 * Created by 黄春怡 on 2017/4/10.
 */
@Repository("gameTypeDao")
public class GameTypeDaoImpl extends BaseDao implements GameTypeDao {
    {
        zclass = new GameType();
    }
}
