package net.onest.time.navigation.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import net.onest.time.R;

import java.util.concurrent.TimeUnit;

public class RegisterActivity extends AppCompatActivity {
    private TextInputEditText registerUser, registerCode, registerPassword, passwordConfirm;
    private Button getCode, btnRegister;

    @SuppressLint("MissingInflateId")
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        findViewById();
        registerUser.setText("shiguang@qq.com");
        registerCode.setText("ZGRW4A");
        registerPassword.setText("123456");
        passwordConfirm.setText("123456");

        setListeners();
    }

    private void setListeners() {
        //注册
        btnRegister.setOnClickListener(view -> {
            String user = registerUser.getText().toString().trim();
            String code = registerCode.getText().toString().trim();
            String password = registerPassword.getText().toString().trim();
            String confirm = passwordConfirm.getText().toString().trim();

            if(user.length()==0 || code.length()==0 || password.length()==0 || confirm.length()==0){
                runOnUiThread(() -> Toast.makeText(this, "不可为空，请重新输入！", Toast.LENGTH_SHORT).show());
            } else if (!code.equals("ZGRW4A")) {
                runOnUiThread(() -> Toast.makeText(this, "验证码有误，请重新输入！", Toast.LENGTH_SHORT).show());
            } else if (!password.equals(confirm)) {
                runOnUiThread(() -> Toast.makeText(this, "两次密码输入不同，请重新输入！", Toast.LENGTH_SHORT).show());
            }else {
                runOnUiThread(() -> Toast.makeText(this, "注册成功，请登录", Toast.LENGTH_SHORT).show());
                finish();
            }
        });

        //获取验证码
        getCode.setOnClickListener(view -> {
            startCountdown();
        });
    }

    private void findViewById() {
        registerUser = findViewById(R.id.edt_register_user);
        registerCode = findViewById(R.id.edt_register_code);
        registerPassword = findViewById(R.id.edt_register_password);
        passwordConfirm = findViewById(R.id.edt_register_password_confirm);
        getCode = findViewById(R.id.btn_getCode);
        btnRegister = findViewById(R.id.btn_register);
    }

    //获取验证码倒计时
    private void startCountdown () {
        new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int seconds = (int) TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished);
                getCode.setText("已发送(" + seconds + "s)");
                //getCode.setTextColor(Color.BLACK);
                getCode.setEnabled(false);
            }

            @Override
            public void onFinish() {
                getCode.setText("获取验证码");
                getCode.setEnabled(true);
            }
        }.start();
    }
}
