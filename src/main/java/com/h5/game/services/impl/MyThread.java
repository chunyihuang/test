package com.h5.game.services.impl;

import com.h5.game.services.interfaces.GameService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by 黄春怡 on 2017/4/27.
 */
public class MyThread extends Thread {
    @Autowired
    private GameService gameService;
    @Override
    public void run() {
        super.run();



    }
}
