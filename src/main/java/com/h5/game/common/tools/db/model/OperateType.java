package com.h5.game.common.tools.db.model;

public enum OperateType {
    /**
     * 等于比较
     */
    EQ(0x001),
    /**
     * 模糊比较
     */
    LIKE(0x002),
    BETWEEN(0x003),
    /**
     * >=
     */
    GE(0x004),
    /**
     * >
     */
    GT(0x005),
    /**
     * <=
     */
    LE(0x006),
    /**
     * <
     */
    LT(0x007),
    /**
     * or 对数组进行遍历查询
     */
    OR(0x008),
    /**
     * field in ( Array )
     */
    IN(0x009);

    private int value ;
    OperateType(int value) {
        this.value = value;
    }

    public int getValue(){
        return value;
    }
}
