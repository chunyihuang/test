package com.h5.game.common.tools.convert.interfaces;

/**
 * Created by 黄春怡 on 2017/4/1.
 */
public interface CustomConvert {

    void registerCustomConvert(Class<?> classType, TransformConvert convert);

    /**
     * 通过属性来获取对应的转换器
     */
    TransformConvert findTransformConvert(Class<?> classType);
}
