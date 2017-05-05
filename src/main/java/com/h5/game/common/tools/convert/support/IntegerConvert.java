package com.h5.game.common.tools.convert.support;

import com.h5.game.common.tools.convert.interfaces.TransformConvert;

/**
 * Created by 黄春怡 on 2017/4/1.
 */
public class IntegerConvert implements TransformConvert {
    public Object convert(String fileName, Object object) {
        try{
            return Integer.valueOf(object.toString());
        }catch (Exception e){
            return null;
        }
    }
}
