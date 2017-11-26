package com.zf.chat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import com.zf.chat.common.Constants;
import com.zf.chat.common.Utils;
import com.zf.chat.view.LoginActivity;

import java.lang.ref.WeakReference;

/**
 * @author zhufeng
 */
public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Boolean isLogin = Utils.getBooleanValue(this, Constants.LOGIN_STATE);
        Handler handler = new SplashHandler(this);
        if (isLogin) {
            getLogin(handler);
        } else {
            handler.sendEmptyMessage(0);
        }
    }

    private void getLogin(Handler handler) {
        String name = Utils.getValue(this, Constants.User_ID);
        String pwd = Utils.getValue(this, Constants.PWD);
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(pwd)) {

        } else {
            Utils.RemoveValue(this, Constants.LOGIN_STATE);
            handler.sendEmptyMessageDelayed(0, 600);
        }
    }

    private static class SplashHandler extends Handler {
        WeakReference<SplashActivity> activity;

        SplashHandler(SplashActivity activity) {
            this.activity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(android.os.Message msg) {
            SplashActivity context = activity.get();
            Boolean isLogin = Utils.getBooleanValue(context, Constants.LOGIN_STATE);
            Intent intent = new Intent();
            if (isLogin) {
                intent.setClass(context, MainActivity.class);
            } else {
                intent.setClass(context, LoginActivity.class);
            }
            context.startActivity(intent);
            context.overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
            context.finish();
        }
    }
}
