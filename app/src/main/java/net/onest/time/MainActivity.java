package net.onest.time;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;


import com.google.android.material.textfield.TextInputEditText;

import net.onest.time.api.UserApi;
import net.onest.time.api.dto.UserDto;
import net.onest.time.navigation.activity.NavigationActivity;
import net.onest.time.navigation.activity.RegisterActivity;
import net.onest.time.navigation.activity.ResetPasswordActivity;

public class MainActivity extends AppCompatActivity{
    private SharedPreferences userInfo;
    private TextInputEditText loginUser, loginPassword;
    private Button forgetPassword, btnLogin, btnRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        loginUser.setText("2808021998@qq.com");
        loginPassword.setText("admin");

        setListeners();
        setAnimator();
    }

    private void setListeners() {
        //登录
        btnLogin.setOnClickListener(view -> {

            String account = loginUser.getText().toString().trim();
            String password = loginPassword.getText().toString().trim();
            if (account.length() == 0 || password.length() == 0){
                Toast.makeText(this, "不可留空，请重新输入！", Toast.LENGTH_SHORT).show();
            } else {
                UserDto userDto = new UserDto();
                userDto.setEmail(account);
                userDto.setPassword(password);
                String token=null;
                try {
                    token = UserApi.login(userDto);
                }catch (Exception e){
                    Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    System.out.println(e.getMessage());
                }
                if(token.isEmpty()){
                    Toast.makeText(this, "账号密码不正确", Toast.LENGTH_SHORT).show();
                }else{

                    Toast.makeText(this, "登录成功，欢迎来到时光！", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, NavigationActivity.class);
                    startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,btnLogin,"fab").toBundle());
                    startActivity(intent);
                }
            }
        });

        //忘记密码
        forgetPassword.setOnClickListener(view -> {
            Intent intent = new Intent(this, ResetPasswordActivity.class);
            startActivity(intent);
        });

        //注册
        btnRegister.setOnClickListener(view -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void findViews() {
        loginUser = findViewById(R.id.edt_login_user);
        loginPassword = findViewById(R.id.edt_login_password);
        forgetPassword = findViewById(R.id.btn_forgetPassword);
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);
    }

    //部件淡出动画效果
    private void setAnimator() {
        // 获取ImageView控件
        // 创建一个透明度动画使其从完全透明（alpha为0）变为完全不透明（alpha为1）
        ObjectAnimator alphaAnimator2 = ObjectAnimator.ofFloat(btnLogin, "alpha", 0f, 1f);
        ObjectAnimator alphaAnimator3 = ObjectAnimator.ofFloat(btnRegister, "alpha", 0f, 1f);
        // 设置动画时长
        alphaAnimator2.setDuration(2000);
        alphaAnimator3.setDuration(2500);
        // 启动动画
        alphaAnimator2.start();
        alphaAnimator3.start();
    }

}