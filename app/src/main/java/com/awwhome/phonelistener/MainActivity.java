package com.awwhome.phonelistener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.awwhome.phonelistener.service.PhoneListenerService;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // 获取上下文
    private Context getContext(){
        return this;
    }

    public void startservice(View view) {

        Intent intent = new Intent();
        intent.setClass(getContext(),PhoneListenerService.class);

        // 启动服务
        startService(intent);
    }
}
