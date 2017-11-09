package com.example.dex.socket;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.IOException;

import dalvik.system.DexClassLoader;

/**
 * Created by kkmike999 on 2017/10/31.
 * <p>
 * 监听Socket，获取传输文件（dex），并执行
 */
public class SocketService extends Service {

    static final int RECEIVE_PORT = 10086;

    SocketReceiver mReceiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            mReceiver = new SocketReceiver(RECEIVE_PORT);

            listen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        try {
            mReceiver.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void listen() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("connecting...");

                    String path = mReceiver.accept(getCacheDir() + "/dex.jar");

                    System.out.println("receive finish. length=" + new File(path).length());

                    // 执行任务
                    execute(path);

                    // 重新监听
                    listen();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 执行jar任务
     *
     * @param path
     */
    private void execute(String path) {
        System.out.println("service execute dex task");

        try {
//            File           dexOutputDir = getDir("dex", 0);
            DexClassLoader cl           = new DexClassLoader(path, getCacheDir().getPath(), null, getClassLoader());

            String taskName = getPackageName() + ".DexTask";
            Class  clazz    = cl.loadClass(taskName);

            Runnable runnable = (Runnable) clazz.newInstance();
            runnable.run();
        } catch (Exception e) {
            e.printStackTrace();
        }

        new File(path).delete();
    }
}
