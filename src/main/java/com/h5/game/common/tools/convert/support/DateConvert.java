package com.h5.game.common.tools.convert.support;

import com.h5.game.common.tools.BaseUtil;
import com.h5.game.common.tools.convert.interfaces.TransformConvert;

import java.text.ParseException;

/**
 * 类用途 <br>
 * create on 2016/8/24
 *
 * @author 任杰
 * @version 1.0
 **/
public class DateConvert implements TransformConvert {
    public Object convert(String fileName,Object object) {
        try {
            return BaseUtil.parseStringToDate(object.toString());
        } catch (ParseException e) {
            return null;
        }
    }
}
