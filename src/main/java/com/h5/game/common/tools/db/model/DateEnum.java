package com.h5.game.common.tools.db.model;


public enum DateEnum {
    START(0x01),END(0x02);

    private int value ;
    DateEnum(int value) {
        this.value = value;
    }

    public int getValue(){
        return value;
    }
}
