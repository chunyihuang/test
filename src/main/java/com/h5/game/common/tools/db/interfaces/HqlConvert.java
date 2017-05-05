package com.h5.game.common.tools.db.interfaces;

import com.h5.game.common.tools.db.base.BaseVo;

/**
 * 类用途 <br>
 * create on 2016/8/23
 *
 * @author 任杰
 * @version 1.0
 **/
public interface HqlConvert {

    <T extends BaseVo> String voToHql(String beanName, T vo);
}
