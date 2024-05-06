package net.onest.time.navigation.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import net.onest.time.R;
import net.onest.time.navigation.activity.PersonEditActivity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class PersonFragment extends Fragment {
    private View view;
    private ImageView userAvatar;
    private LinearLayout userEdit;
    private TextView userName, userId, userCreateAt, userTotalDay;
    private TextView userTodayComplete, userTotalComplete;
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
            startActivity(intent);
        });
    }

    @SuppressLint("SetTextI18n")
    private void initData() {

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

    private void findViewById(View view) {
        userAvatar = view.findViewById(R.id.user_avatar);
        userEdit = view.findViewById(R.id.user_edit);
        userName = view.findViewById(R.id.user_name);
        userId = view.findViewById(R.id.user_id);
        userCreateAt = view.findViewById(R.id.user_create_time);
        userTotalDay = view.findViewById(R.id.user_total_day);
        userTodayComplete = view.findViewById(R.id.user_today_complete);
        userTotalComplete = view.findViewById(R.id.user_total_complete);
    }
}
