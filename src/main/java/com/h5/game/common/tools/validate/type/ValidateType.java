package com.h5.game.common.tools.validate.type;

/**
 * Created by 黄春怡 on 2017/4/1.
 */
public enum ValidateType {
    /**
     * 无需配置,默认,非空校验
     */
    Required,
    /**
     * 长度校验,验证长度最大最小,需要配置
     */
    Length,
    /**
     * 自定义校验,使用正则表达式进行校验
     */
    Custom
}
