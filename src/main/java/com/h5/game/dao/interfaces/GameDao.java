package com.h5.game.dao.interfaces;

import com.h5.game.dao.base.IBaseDao;
import com.h5.game.dao.base.PageResults;
import com.h5.game.model.vo.QueryGameVo;

/**
 * Created by 黄春怡 on 2017/4/10.
 */
public interface GameDao  extends IBaseDao {

    PageResults listGames(QueryGameVo queryGameVo, Integer page, Integer rows);


}
