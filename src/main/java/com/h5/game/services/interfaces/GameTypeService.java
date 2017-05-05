package com.h5.game.services.interfaces;

import com.h5.game.model.bean.GameType;

import java.util.List;
import java.util.Map;

/**
 * Created by 黄春怡 on 2017/3/31.
 */

public interface GameTypeService {

    Map saveOrUpdateUser(GameType gameType);

    List listAll();

    Boolean removeAdmin(Integer id);
}
