package com.h5.game.common.tools.validate.interfaces;

import com.h5.game.common.tools.validate.annotations.Validate;
import com.h5.game.common.tools.validate.model.ValidateResult;

/**
 * Created by 黄春怡 on 2017/4/1.
 */
public interface ValidateClass {

    /**
     * 校验bean是否符合输入
     */
    ValidateResult validate(Object bean);

    /**
     * 校验bean是否符合输入,指定需要校验的url
     */
    ValidateResult validate(Object bean, String url);

    /**
     * 校验值是否符合validate注解设置的校验
     */
    ValidateResult validate(Object value, Validate validate, String fileName);
}
