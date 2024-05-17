package net.onest.time.navigation.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import net.onest.time.R;
import net.onest.time.api.UserApi;
import net.onest.time.api.dto.UserDto;

import java.util.concurrent.TimeUnit;

import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {
    private TextInputEditText registerNickname, registerEmail, registerCode, registerPassword, passwordConfirm;
    private Button getCode, btnRegister;
    private String registerCodeKey;

    @SuppressLint("MissingInflateId")
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        findViewById();
        setListeners();
    }

    private void setListeners() {
        //注册
        btnRegister.setOnClickListener(view -> {
            String nickName = registerNickname.getText().toString().trim();
            String email = registerEmail.getText().toString().trim();
            String code = registerCode.getText().toString().trim();
            String password = registerPassword.getText().toString().trim();
            String confirm = passwordConfirm.getText().toString().trim();

            if(email.length()==0 || code.length()==0 || password.length()==0 || confirm.length()==0){
                Toast.makeText(this, "不可为空，请重新输入！", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(confirm)) {
                Toast.makeText(this, "两次密码输入不同，请重新输入！", Toast.LENGTH_SHORT).show();
            }else {
                UserDto userDto = new UserDto();
                userDto.setUserName(nickName);
                userDto.setEmail(email);
                userDto.setPassword(password);
                userDto.setConfirmPassword(confirm);
                userDto.setEmailCodeKey(registerCodeKey);
                userDto.setEmailCode(code);
                UserApi.register(userDto);
                Toast.makeText(this, "注册成功，请登录", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        //获取验证码
        getCode.setOnClickListener(view -> {
            registerCodeKey = UserApi.getEmailCodeKey(registerEmail.getText().toString().trim());
            startCountdown();
        });
    }

    private void findViewById() {
        registerNickname = findViewById(R.id.edt_register_nickname);
        registerEmail = findViewById(R.id.edt_register_email);
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
