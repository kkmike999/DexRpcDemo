package com.example.dex;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.example.dex.socket.SocketService;

/**
 * Created by kkmike999 on 2017/11/09.
 */
public class App extends Application {

    static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();

        sContext = this;

        Intent intent = new Intent(this, SocketService.class);
        startService(intent);
    }

    public static Context getContext() {
        return sContext;
    }
}
