package com.zf.chat.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zf.chat.MainActivity;
import com.zf.chat.R;
import com.zf.chat.common.Utils;

//注册
public class RegisterActivity extends BaseActivity implements OnClickListener {
    private TextView txt_title;
    private ImageView img_back;
    private Button btn_register, btn_send;
    private EditText et_usertel, et_password, et_code;
    private MyCount mc;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_register);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initControl() {
        txt_title = (TextView) findViewById(R.id.txt_title);
        txt_title.setText("注册");
        img_back = (ImageView) findViewById(R.id.img_back);
        img_back.setVisibility(View.VISIBLE);
        btn_send = (Button) findViewById(R.id.btn_send);
        btn_register = (Button) findViewById(R.id.btn_register);
        et_usertel = (EditText) findViewById(R.id.et_usertel);
        et_password = (EditText) findViewById(R.id.et_password);
        et_code = (EditText) findViewById(R.id.et_code);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
    }

    @Override
    protected void setListener() {
        img_back.setOnClickListener(this);
        btn_send.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        et_usertel.addTextChangedListener(new TelTextChange());
        et_password.addTextChangedListener(new TextChange());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finishWithAnim();
                break;
            case R.id.btn_send:
                if (mc == null) {
                    mc = new MyCount(60000, 1000); // 第一参数是总的时间，第二个是间隔时间
                }
                mc.start();
                break;
            case R.id.btn_register:
                getRegister();
                break;
            default:
                break;
        }
    }

    private void getRegister() {
        final String name = et_usertel.getText().toString();
        final String pwd = et_password.getText().toString();
        String code = et_code.getText().toString();
        if (!Utils.isMobileNO(name)) {
            Toast.makeText(context, R.string.tip_use_phone_number, Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(pwd)) {
            Toast.makeText(context, R.string.tip_user_password, Toast.LENGTH_LONG).show();
            return;
        }
        getLoadingDialog("正在注册...  ").show();
        btn_register.setEnabled(false);
        btn_send.setEnabled(false);

    }

    // 手机号 EditText监听器
    class TelTextChange implements TextWatcher {

        @Override
        public void afterTextChanged(Editable arg0) {

        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

        }

        @Override
        public void onTextChanged(CharSequence cs, int start, int before, int count) {
            String phone = et_usertel.getText().toString();
            if (phone.length() == 11) {
                if (Utils.isMobileNO(phone)) {
                    btn_send.setBackground(getResources().getDrawable(R.drawable.btn_bg_green));
                    btn_send.setTextColor(0xFFFFFFFF);
                    btn_send.setEnabled(true);
                    btn_register.setBackground(getResources().getDrawable(R.drawable.btn_bg_green));
                    btn_register.setTextColor(0xFFFFFFFF);
                    btn_register.setEnabled(true);
                } else {
                    et_usertel.requestFocus();
                    Toast.makeText(context, R.string.tip_phone_number, Toast.LENGTH_LONG).show();
                }
            } else {
                btn_send.setBackground(getResources().getDrawable(R.drawable.btn_enable_green));
                btn_send.setTextColor(0xFFD0EFC6);
                btn_send.setEnabled(false);
                btn_register.setBackground(getResources().getDrawable(R.drawable.btn_enable_green));
                btn_register.setTextColor(0xFFD0EFC6);
                btn_register.setEnabled(true);
            }
        }
    }

    // EditText监听器
    class TextChange implements TextWatcher {

        @Override
        public void afterTextChanged(Editable arg0) {

        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

        }

        @Override
        public void onTextChanged(CharSequence cs, int start, int before, int count) {
            boolean Sign1 = et_code.getText().length() > 0;
            boolean Sign2 = et_usertel.getText().length() > 0;
            boolean Sign3 = et_password.getText().length() > 0;

            if (Sign1 & Sign2 & Sign3) {
                btn_register.setBackground(getResources().getDrawable(R.drawable.btn_bg_green));
                btn_register.setTextColor(0xFFFFFFFF);
                btn_register.setEnabled(true);
            } else {
                btn_register.setBackground(getResources().getDrawable(R.drawable.btn_enable_green));
                btn_register.setTextColor(0xFFD0EFC6);
                btn_register.setEnabled(false);
            }
        }
    }

    /* 定义一个倒计时的内部类 */
    private class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            btn_send.setEnabled(true);
            btn_send.setText("发送验证码");
        }

        @Override
        public void onTick(long millisUntilFinished) {
            btn_send.setEnabled(false);
            btn_send.setText("(" + millisUntilFinished / 1000 + ")秒");
        }
    }

    private void initUserList() {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
        finish();
    }
}
