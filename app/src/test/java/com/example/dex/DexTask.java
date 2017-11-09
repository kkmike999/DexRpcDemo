package com.example.dex;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by kkmike999 on 2017/11/06.
 */
public class DexTask implements Runnable {
    @Override
    public void run() {
        System.out.println("DexTask running...");

        EventBus.getDefault().post("收到dex并执行");
    }
}
