package net.onest.time.navigation.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import net.onest.time.R;

public class PrivacyPolicyActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy_summary_page);
        //初始化控件
        initData();
    }
//
//    //返回按钮的点击事件
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if (item.getItemId() == android.R.id.home) {
//            finish();
//        }
//        return true;
//    }

    //初始化控件
    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    private void initData() {
        //设置标题栏
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            //设置Bar标题
            actionBar.setTitle("隐私政策");
        }
        WebView webview = findViewById(R.id.ll_agreement);
        webview.setBackgroundColor(Color.TRANSPARENT);
        webview.loadUrl("file:///android_asset/privacy_policy.html");
    }
}
