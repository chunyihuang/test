package com.h5.game.common.tools.db.base;

import com.h5.game.common.tools.db.model.DateTimeEntity;
import com.h5.game.common.tools.db.model.KVParam;
import org.hibernate.criterion.MatchMode;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;


public abstract class OperateAdapter {
    protected abstract void addLeOrLt(KVParam<String,Object> param, boolean isEq, Object adapterParam);

    protected abstract void addGeOrGt(KVParam<String,Object> param, boolean isEq, Object adapterParam);

    protected abstract void addBetween(KVParam<String,Object> param, Field field, HashMap<Integer,DateTimeEntity> dateTimeEntityHashMap, String splitChar, Object adapterParam);

    protected abstract void addLike(KVParam<String,Object> param, MatchMode matchMode, Object adapterParam);

    protected abstract void addEq(KVParam<String,Object> param, Object adapterParam);

    protected abstract void addOr(KVParam<String, Object> param, HashMap<Integer, List<KVParam>> orParamModelHashMap, int i, Object adapterParam);

    protected abstract void addIn(KVParam<String,Object> param, Object adapterParam);
}
