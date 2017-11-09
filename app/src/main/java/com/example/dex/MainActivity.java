package com.example.dex;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tbruyelle.rxpermissions.RxPermissions;

import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 申请权限
        new RxPermissions(this).request(Manifest.permission_group.STORAGE, Manifest.permission.INTERNET)
                               .subscribe(new Action1<Boolean>() {
                                   @Override
                                   public void call(Boolean aBoolean) {}
                               });
    }
}
