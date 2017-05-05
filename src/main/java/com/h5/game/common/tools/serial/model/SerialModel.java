package com.h5.game.common.tools.serial.model;

/**
 * Created by 黄春怡 on 2017/4/1.
 */
public final class SerialModel {

    private int serial;

    public SerialModel() {
    }

    public SerialModel(int serial) {
        this.serial = serial;
    }

    public int getSerial() {
        return serial;
    }

    public void setSerial(int serial) {
        this.serial = serial;
    }

    public void add(int addNum){
        serial = serial + addNum;
    }
}
