package com.h5.game.common.tools.convert.support;

import com.h5.game.common.tools.convert.interfaces.TransformConvert;

/**
 * 类用途 <br>
 * create on 2016/8/24
 *
 * @author 任杰
 * @version 1.0
 **/
public class BooleanConvert implements TransformConvert {
    public Object convert(String fileName,Object object) {
        try{
            return Boolean.valueOf(object.toString());
        }catch (Exception e){
            return null;
        }
    }
}
