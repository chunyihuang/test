package com.h5.game.common.tools.serial.support;

import com.h5.game.common.tools.serial.model.SerialModel;
import org.apache.commons.collections.FastHashMap;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 黄春怡 on 2017/4/1.
 */
public final class SerialNumberManage {

    private FastHashMap serialCache = new FastHashMap();

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

    private static final Object _lock = new Object();

    private final int initSerial = 0;

    private int formatBitCount = 3;

    public SerialNumberManage() {
        serialCache.setFast(true);
    }

    private int getSerial(String dateKey, boolean autoAdd){
        Object serialObj = serialCache.get(dateKey); //在map中获取对象
        if(serialObj == null){ //对象不存在开始执行初始化操作
            serialCache.clear();
            serialObj = new SerialModel(initSerial);
            serialCache.put(dateKey,serialObj);
        }
        SerialModel serialModel = (SerialModel)serialObj;
        if(autoAdd){ //如果需要增加,则执行增加操作
            serialModel.add(1);
        }
        return serialModel.getSerial();
    }

    /**
     * 获取下一位流水号
     * @return 返回流水号
     */
    public String getNextSerialNumber(){
        Date today = new Date();
        String dateKey = dateFormat.format(today);
        int serial;
        synchronized (_lock){
            serial = getSerial(dateKey,true);
        }
        return dateKey + fillZero(serial);
    }

    /**
     * 获取当前的流水号,不做自增操作
     * @return 返回当前流水号
     */
    public String getCurrentSerialNumber() {
        Date today = new Date();
        String dateKey = dateFormat.format(today);
        int serial;
        synchronized (_lock){
            serial = getSerial(dateKey,false);
        }
        return dateKey + fillZero(serial);
    }

    /**
     * 初始化编号参数,获取序列号的时候会由编号下一位开始
     * @param serialNumber 完整的流水编号
     */
    public void initSerialNumber(String serialNumber){
        if(serialNumber != null && serialNumber.length() == (8 + formatBitCount)){
            synchronized (_lock){
                String serialStr = serialNumber.substring(8);
                String day = serialNumber.substring(0,8);
                try {
                    serialCache.put(day,new SerialModel(Integer.valueOf(serialStr)));
                }catch (Exception e){
                    System.out.println("init serial manage error : " + e.getMessage());
                }
            }
        }
    }

    /**
     * 设置后面的位数
     * @param count 位数,不能小于1
     */
    public void setLastNumCount(int count) {
        if(count < 1) return;
        formatBitCount = count;
    }

    public void paremcTest(){
        serialCache.put("test",new SerialModel(initSerial));
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000000; i ++){
            serialCache.get("test");
        }
        long end = System.currentTimeMillis();
        System.out.println("using time : " + (end - start));
    }

    private String fillZero(int serial){
        String str = serial + "";
        int serialL = str.length();
        int itrCount = formatBitCount - serialL;
        for (int i = 0; i < itrCount; i ++){
            str = '0' + str;
        }
        return str;
    }
}
