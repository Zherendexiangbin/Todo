package net.onest.time.navigation.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import net.onest.time.R;
import net.onest.time.navigation.activity.PersonEditActivity;
import net.onest.time.navigation.activity.StudyRoomActivity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class PersonActivity extends AppCompatActivity {
    private ImageView userAvatar;
    private LinearLayout userEdit;
    private TextView userName, userId, userCreateAt, userTotalDay;
    private TextView userTodayComplete, userTotalComplete;
    
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_fragment);
        
        findViewById();
        initData();
        setListeners();
    }

    private void setListeners() {
        //点击进入用户信息编辑页面
        userEdit.setOnClickListener(view -> {
            Intent intent = new Intent(this, PersonEditActivity.class);
            startActivity(intent);
        });
    }

    @SuppressLint("SetTextI18n")
    private void initData() {
        //暂定
        userAvatar.setOnClickListener(view -> {
            Intent intent = new Intent(this, StudyRoomActivity.class);
            startActivity(intent);
        });

        //用户昵称
        userName.setText("时光1");

        //用户Id
        userId.setText("UID：" + "20240424");

        //用户创建时间及应用使用时间
        userCreateAt.setText("2020年4月24日");
        LocalDate localDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年M月d日");
        LocalDate specifiedDate = LocalDate.parse(userCreateAt.getText().toString().trim(), formatter);
        userTotalDay.setText(Long.toString(ChronoUnit.DAYS.between(specifiedDate, localDate)));

        //用户今日完成日程数及累计完整日程数
        userTodayComplete.setText("今日完成日程：" + 3);
        userTotalComplete.setText("累计完成日程：" + 1144);
    }

    private void findViewById() {
        userAvatar = findViewById(R.id.user_avatar);
        userEdit = findViewById(R.id.user_edit);
        userName = findViewById(R.id.user_name);
        userId = findViewById(R.id.user_id);
        userCreateAt = findViewById(R.id.user_create_time);
        userTotalDay = findViewById(R.id.user_total_day);
        userTodayComplete = findViewById(R.id.user_today_complete);
        userTotalComplete = findViewById(R.id.user_total_complete);
    }
}
