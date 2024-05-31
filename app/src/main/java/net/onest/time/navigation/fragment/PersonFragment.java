package net.onest.time.navigation.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import net.onest.time.MainActivity;
import net.onest.time.R;
import net.onest.time.TimerActivity;
import net.onest.time.api.ServerConstant;
import net.onest.time.api.UserApi;
import net.onest.time.api.vo.UserVo;
import net.onest.time.navigation.activity.NavigationActivity;
import net.onest.time.navigation.activity.PersonEditActivity;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class PersonFragment extends Fragment {
    private View view;
    private ImageView userAvatar;
    private LinearLayout userEdit;
    private TextView userName, userId, userCreateAt, userTotalDay;
    private TextView userTodayComplete, userTotalComplete;
    private Button change,exit;

    private static final int INTENT_CODE = 1;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.person_fragment, container, false);
        findViewById(view);
        initData();
        setListeners();
        return view;
    }

    private void setListeners() {
        //点击进入用户信息编辑页面
        userEdit.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), PersonEditActivity.class);
            startActivityForResult(intent, INTENT_CODE);
        });

        //切换账号:
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(requireContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//它可以关掉所要到的界面中间的activity
                requireContext().startActivity(intent);
            }
        });
        //退出登录:
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(requireContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//它可以关掉所要到的界面中间的activity
                requireContext().startActivity(intent);
            }
        });
    }

    //处理页面跳转返回结果
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        initData();
    }

    @SuppressLint("SetTextI18n")
    private void initData() {
        //获取用户信息
        UserVo userVo = UserApi.getUserInfo();

        //用户头像
        Glide.with(requireContext())
                .load(userVo.getAvatar())
                        .into(userAvatar);
        //用户昵称
        userName.setText(userVo.getUserName());
        //用户Id
        userId.setText("UID：" + userVo.getUserId());

        //用户创建时间及应用使用时间
        @SuppressLint("SimpleDateFormat")
        String format = new SimpleDateFormat("yyyy年MM月dd日").format(userVo.getCreatedAt());
        userCreateAt.setText(format);

        LocalDate localDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年M月d日");
        LocalDate specifiedDate = LocalDate.parse(userCreateAt.getText().toString().trim(), formatter);
        userTotalDay.setText(Long.toString(ChronoUnit.DAYS.between(specifiedDate, localDate)));

        //用户今日完成日程数及累计完整日程数
        userTodayComplete.setText("今日完成日程：" + 3);
        userTotalComplete.setText("累计完成日程：" + 1144);
    }

    private void findViewById(View view) {
        userAvatar = view.findViewById(R.id.user_avatar);
        userEdit = view.findViewById(R.id.user_edit);
        userName = view.findViewById(R.id.user_name);
        userId = view.findViewById(R.id.user_id);
        userCreateAt = view.findViewById(R.id.user_create_time);
        userTotalDay = view.findViewById(R.id.user_total_day);
        userTodayComplete = view.findViewById(R.id.user_today_complete);
        userTotalComplete = view.findViewById(R.id.user_total_complete);

        change = view.findViewById(R.id.btn_change);
        exit = view.findViewById(R.id.btn_exit);
    }
}
