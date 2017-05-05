package com.h5.game.common.tools.db.annotations;

import com.h5.game.common.tools.db.model.OperateType;
import org.hibernate.criterion.MatchMode;
import org.springframework.web.bind.annotation.Mapping;

import java.lang.annotation.*;

/**
 * Created by 黄春怡 on 2017/4/1.
 */
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Mapping
public @interface Operate {
    OperateType value() default OperateType.EQ;
    MatchMode mode() default MatchMode.ANYWHERE;
    String splitChar() default ",";
    int groupId() default 0;
}
