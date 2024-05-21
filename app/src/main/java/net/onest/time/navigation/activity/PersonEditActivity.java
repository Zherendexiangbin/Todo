package net.onest.time.navigation.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;

import net.onest.time.R;
import net.onest.time.api.UserApi;
import net.onest.time.api.dto.UserDto;
import net.onest.time.api.vo.UserVo;

import java.util.Calendar;

import cn.qqtheme.framework.picker.OptionPicker;

public class PersonEditActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    private ImageView userAvatar;
    private Button btnBack, btnSave;
    private EditText nickName, signature;
    private TextView userUID;
    private TextView sex, birthday, career, area;
    private String avatarUrl;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private boolean havePermission = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_person_edit_page);
        getView();
        renderUserInfo();
        initListener();
    }

    /**
     * 渲染用户信息
     */
    private void renderUserInfo() {
        UserVo userVo = UserApi.getUserInfo();
        //用户头像
        Glide.with(PersonEditActivity.this)
                .load(userVo.getAvatar())
                .into(userAvatar);
        //用户呢称
        nickName.setText(userVo.getUserName());
        //用户UID
        userUID.setText(" " + userVo.getUserId());
        //用户签名
        signature.setText(userVo.getSignature());
    }

    private void getView() {
        btnBack = findViewById(R.id.btn_back);
        btnSave = findViewById(R.id.btn_save);
        userAvatar = findViewById(R.id.edit_avatar);//头像
        nickName = findViewById(R.id.edit_nickname);//昵称
        userUID = findViewById(R.id.user_UID);
//        sex = findViewById(R.id.edit_sex);//性别
        signature = findViewById(R.id.edit_signature);//签名
//        birthday = findViewById(R.id.edit_birthday);//生日
//        career = findViewById(R.id.edit_career);//职业
//        area = findViewById(R.id.edit_area);//所在地区
    }

    private void initListener() {
        //返回、保存按钮监听
        OnCustomClickListener listener = new OnCustomClickListener();
        btnBack.setOnClickListener(listener);
        btnSave.setOnClickListener(listener);
        userAvatar.setOnClickListener(listener);
//        sex.setOnClickListener(listener);
        nickName.setOnClickListener(listener);
        signature.setOnClickListener(listener);
//        birthday.setOnClickListener(listener);
//        career.setOnClickListener(listener);
//        area.setOnClickListener(listener);
    }


    class OnCustomClickListener implements View.OnClickListener {
        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_back:
                    //返回按钮点击事件
                    finish();
                    break;
                case R.id.btn_save:
                    //保存按钮点击事件
                    updateUserInfo();
                    Toast.makeText(PersonEditActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.edit_avatar:
                    checkPermission();
                    if (havePermission) {
                        //点击修改头像
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, 1);
                    }
                    break;
//                case R.id.edit_birthday:
//                    //生日选择点击事件
//                    Calendar calendar = Calendar.getInstance();//获取Calendar实例
//                    //创建日期选择器
//                    DatePickerDialog dialog = new DatePickerDialog(PersonEditActivity.this, PersonEditActivity.this,
//                            calendar.get(Calendar.YEAR),
//                            calendar.get(Calendar.MARCH),
//                            calendar.get(Calendar.DAY_OF_MONTH));
//                    dialog.show();//窗口弹出
//                    break;
//                case R.id.edit_sex:
//                    //性别选择点击事件
//                    selectSex();
//                    break;
//                case R.id.edit_career:
//                    //职业选择点击事件
//                    selectCareer();
//                    break;
//                case R.id.edit_area:
//                    //地区选择点击事件
//                    selectArea();
//                    break;
            }
        }
    }

    // 更新用户的信息
    private void updateUserInfo() {
        UserApi.modifyAvatar(avatarUrl);//用户头像
        UserApi.modifyUserName(nickName.getText().toString().trim());//用户名
        UserApi.modifySignature(signature.getText().toString().trim());//用户个性签名
    }

    //生日、年龄选择
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        String desc = String.format("%d-%d-%d", year, month + 1, dayOfMonth);
        birthday.setText(desc);
        //计算年龄 LocalDate.now().minusYears(year).getYear()+"岁"
    }

    //调用本地相册
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            try {
                avatarUrl = selectedImageUri + "";
                userAvatar.setImageURI(selectedImageUri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    //获取图片路径
    private Pair<String, String> getImagePath(Uri avatarUri) {
        Cursor cursor = getContentResolver()
                .query(avatarUri, new String[]{MediaStore.Images.Media.DATA, MediaStore.Images.Media.MIME_TYPE}, null, null, null);
        assert cursor != null;
        cursor.moveToFirst();
        return new Pair<>(cursor.getString(0), cursor.getString(1));
    }

    //职业选择
    private void selectCareer() {
        OptionPicker picker = new OptionPicker(PersonEditActivity.this, new String[]{
                "在读学生", "服务型职业", "技术型职业", "商业型职业",
                "艺术型职业", "体育型职业", "社会公益型职业", "政府管理型职业",
                "农业和渔业型职业", "制造业和建筑业型职业"
        });
        picker.setOffset(2);//第一个选项相对于顶部的偏移量
        picker.setSelectedIndex(0);//设置默认选中项索引
        picker.setTextSize(15);//设置选项文本大小
        picker.setTextColor(Color.BLACK);
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(String option) {
                showToast(option);
            }

            private void showToast(String option) {
                career.setText(option);
            }
        });
        picker.show();
    }

    //性别选择
    private void selectSex() {
        OptionPicker picker = new OptionPicker(PersonEditActivity.this, new String[]{
                "男", "女", "保密"
        });
        picker.setOffset(2);
        picker.setSelectedIndex(0);
        picker.setTextSize(15);
        picker.setTextColor(Color.BLACK);
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(String option) {
                showToast(option);
            }

            private void showToast(String option) {
                sex.setText(option);
            }
        });
        picker.show();
    }

    //地区选择
    private void selectArea() {
        OptionPicker picker = new OptionPicker(PersonEditActivity.this, new String[]{
                "北京", "天津", "上海", "重庆", "香港", "澳门",
                "河北", "山西", "辽宁省", "吉林", "黑龙江", "江苏", "浙江",
                "安徽", "福建", "江西", "山东", "河南", "湖南", "广东",
                "海南", "四川", "贵州", "云南", "陕西", "甘肃", "青海", "台湾",
                "内蒙古自治区", "宁夏回族自治区", "新疆维吾尔自治区", "广西壮族自治区", "西藏自治区",
                "新疆生产建设兵团"
        });
        picker.setOffset(2);
        picker.setSelectedIndex(0);
        picker.setTextSize(15);
        picker.setTextColor(Color.BLACK);
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(String option) {
                showToast(option);
            }

            private void showToast(String option) {
                area.setText(option);
            }
        });
        picker.show();
    }

    private void checkPermission() {
        //检查权限（NEED_PERMISSION）是否被授权 PackageManager.PERMISSION_GRANTED表示同意授权
        if (Build.VERSION.SDK_INT >= 30) {
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivity(intent);
            } else {
                havePermission = true;
                Log.i("swyLog", "Android 11以上，当前已有权限");
            }
        } else {
            // sdk dev 29
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    ActivityCompat.requestPermissions(PersonEditActivity.this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
                } else {
                    havePermission = true;
                    Log.i("swyLog", "Android 6.0以上，11以下，当前已有权限");
                }
            } else {
                havePermission = true;
                Log.i("swyLog", "Android 6.0以下，已获取权限");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    havePermission = true;
                    //点击修改头像
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 1);
                } else {
                    havePermission = false;
                    Toast.makeText(PersonEditActivity.this, "请授予存储空间的权限", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
