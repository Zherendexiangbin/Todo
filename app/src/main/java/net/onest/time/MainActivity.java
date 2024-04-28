package net.onest.time;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ObjectAnimator;
import android.app.ActivityOptions;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.textfield.TextInputEditText;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.mut_jaeryo.circletimer.CircleTimer;

import net.onest.time.entity.Item;
import net.onest.time.navigation.activity.NavigationActivity;
import net.onest.time.navigation.activity.RegisterActivity;
import net.onest.time.navigation.activity.ResetPasswordActivity;
import net.onest.time.navigation.fragment.PersonActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{
    private TextInputEditText loginUser, loginPassword;
    private Button forgetPassword, btnLogin, btnRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        loginUser.setText("shiguang@qq.com");
        loginPassword.setText("123456");

        setListeners();
        setAnimator();
//
//        btn = findViewById(R.id.btn);
//
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                calendarView.scrollToCurrent();//回到“今天日期”
//                Intent intent = new Intent();
//                intent.setClass(MainActivity.this, NavigationActivity.class);
//                startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,btn,"fab").toBundle());
//            }
//        });
    }

    private void setListeners() {
        //登录
        btnLogin.setOnClickListener(view -> {
            String user = loginUser.getText().toString().trim();
            String password = loginPassword.getText().toString().trim();

            if (user.length() == 0 || password.length() == 0){
                runOnUiThread(() -> Toast.makeText(this, "不可留空，请重新输入！", Toast.LENGTH_SHORT).show());
            } else if (!password.equals("123456")) {
                runOnUiThread(() -> Toast.makeText(this, "密码错误，请重新输入！", Toast.LENGTH_SHORT).show());
            }else {
                runOnUiThread(() -> Toast.makeText(this, "登录成功，欢迎来到时光！", Toast.LENGTH_SHORT).show());
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, NavigationActivity.class);
                startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,btnLogin,"fab").toBundle());
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