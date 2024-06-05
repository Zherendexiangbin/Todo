package net.onest.time;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import net.onest.time.api.UserApi;
import net.onest.time.api.dto.UserDto;
import net.onest.time.api.vo.UserVo;

public class AccountSafeActivity extends AppCompatActivity {
    private Button back,modify,sendVerify,sign;
    private EditText passwordOne,passwordTwo,verification;
    private RelativeLayout modifyPass,logOut;
    private LinearLayout modifyPage;
    private UserVo userVo;
    private String codeKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_safe);
        userVo = UserApi.getUserInfo();

        findViews();

        modifyPage.setVisibility(View.GONE);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        modifyPass.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {
                if(modifyPage.getVisibility()==View.GONE){
                    modifyPage.setVisibility(View.VISIBLE);
                    sign.setBackground(getResources().getDrawable(R.drawable.arrow_down2));

                }else{
                    modifyPage.setVisibility((View.GONE));
                    sign.setBackground(getResources().getDrawable(R.drawable.right_arrow));
                }
            }
        });

        //修改页面显示出来：
        sendVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codeKey = UserApi.getEmailCodeKey(userVo.getEmail());
                Toast.makeText(AccountSafeActivity.this, "你的验证码是 " + codeKey , Toast.LENGTH_SHORT).show();
                new CountDownTimer(60000,1000){

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onTick(long millisUntilFinished) {
                        sendVerify.setText(millisUntilFinished+"");
                    }

                    @Override
                    public void onFinish() {
                        sendVerify.setText("验证码");
                    }
                };
            }
        });

        //注销账户
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserApi.logout();
                Intent intent = new Intent(AccountSafeActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        
        //修改密码:
        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(verification.getText().toString().isEmpty()){
                    Toast.makeText(AccountSafeActivity.this, "验证码已发送至QQ邮箱，请填写~", Toast.LENGTH_SHORT).show();
                }else if(passwordOne.getText().toString().trim().isEmpty()||
                        !passwordOne.getText().toString().trim().equals(passwordTwo.getText().toString().trim())){
                    Toast.makeText(AccountSafeActivity.this, "两次密码不一致！请确定~", Toast.LENGTH_SHORT).show();
                }else{
                    UserDto userDto = new UserDto();
                    userDto.setPassword(passwordOne.getText().toString().trim());
                    userDto.setConfirmPassword(passwordTwo.getText().toString().trim());
                    userDto.setEmailCodeKey(codeKey);
                    userDto.setEmailCode(verification.getText().toString().trim());
                    UserApi.modifyPassword(userDto);
                    Toast.makeText(AccountSafeActivity.this, "修改成功😊", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(AccountSafeActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void findViews() {
        back = findViewById(R.id.back);
        modify = findViewById(R.id.modify);
        sendVerify = findViewById(R.id.send_verify);
        passwordOne = findViewById(R.id.password_one);
        passwordTwo = findViewById(R.id.password_two);
        verification = findViewById(R.id.verification);
        modifyPass = findViewById(R.id.modify_password);
        logOut = findViewById(R.id.log_out);
        modifyPage = findViewById(R.id.modify_page);
        sign = findViewById(R.id.sign);
    }
}