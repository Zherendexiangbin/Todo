package net.onest.time;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.android.material.textfield.TextInputEditText;

import net.onest.time.api.UserApi;
import net.onest.time.api.dto.UserDto;
import net.onest.time.api.utils.RequestUtil;
import net.onest.time.api.vo.UserVo;
import net.onest.time.constant.SharedPreferencesConstant;
import net.onest.time.constant.UserInfoConstant;
import net.onest.time.navigation.activity.NavigationActivity;
import net.onest.time.navigation.activity.RegisterActivity;
import net.onest.time.navigation.activity.ResetPasswordActivity;
import net.onest.time.utils.StringUtil;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ImageView logo;
    private TextInputEditText loginUser, loginPassword;
    private Button forgetPassword, btnLogin, btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        findViews();
        Glide.with(MainActivity.this)
                        .load(R.mipmap.logo2)
                        .transition(DrawableTransitionOptions.withCrossFade(2000))
                        .into(logo);

        autoLogin();

//        loginUser.setText("2808021998@qq.com");
//        loginPassword.setText("admin");
        loginUser.setText("212296944@qq.com");
        loginPassword.setText("123456");

        setListeners();
        setAnimator();
    }

    /**
     * 通过token自动登录
     */
    private void autoLogin() {
        UserVo userInfo = UserApi.getUserInfo();
        if (userInfo != null) {
            getApplication()
                    .getApplicationContext()
                    .getSharedPreferences(SharedPreferencesConstant.USER_INFO, Context.MODE_PRIVATE)
                    .edit()
                    .putString(UserInfoConstant.USER_INFO, RequestUtil.getGson().toJson(userInfo))
                    .apply();

            loginSuccess();
        }
    }

    private void setListeners() {
        //登录
        btnLogin.setOnClickListener(view -> {

            String account = loginUser.getText().toString().trim();
            String password = loginPassword.getText().toString().trim();

            if (account.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "不可留空，请重新输入！", Toast.LENGTH_SHORT).show();
            } else {
                UserDto userDto = new UserDto();
                userDto.setEmail(account);
                userDto.setPassword(password);
                String token = null;
                try {
                    token = UserApi.login(userDto);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
                if (token == null || token.isEmpty()) {
                    Toast.makeText(this, "账号密码不正确", Toast.LENGTH_SHORT).show();
                } else {
                    loginSuccess();
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


        // 前端校验手机号邮箱
        loginUser.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String principal = loginUser.getText().toString();
                if (principal.isEmpty() || (!StringUtil.isEmail(principal) && !StringUtil.isPhone(principal))) {
                    loginUser.setError("请输入正确的手机号或邮箱！");
                } else {
                    loginUser.setError(null);
                }
            }
        });

        // 前端校验密码为空
        loginPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String credential = loginPassword.getText().toString();
                if (credential.isEmpty()) {
                    loginPassword.setError("请输入密码！");
                } else {
                    loginPassword.setError(null);
                }
            }
        });
    }

    private void findViews() {
        logo = findViewById(R.id.login_logo);
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

    private void loginSuccess() {
//        Toast.makeText(this, "登录成功，欢迎来到时光！", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, NavigationActivity.class);
        startActivity(intent);
        finish();
    }
}