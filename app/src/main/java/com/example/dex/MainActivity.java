package com.example.dex;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.tbruyelle.rxpermissions.RxPermissions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_hello)
    TextView tv_hello;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        // 申请权限
        new RxPermissions(this).request(Manifest.permission_group.STORAGE, Manifest.permission.INTERNET)
                               .subscribe(new Action1<Boolean>() {
                                   @Override
                                   public void call(Boolean aBoolean) {}
                               });

        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMsgEvent(String msg) {
        tv_hello.setText(msg);
    }
}
