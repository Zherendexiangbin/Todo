package net.onest.time.navigation.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import net.onest.time.R;

public class AboutAppActivity extends AppCompatActivity {
    private Button back;
    private TextView checkVersion;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_app_about_page);

        back = findViewById(R.id.back);
        checkVersion = findViewById(R.id.check_version);

        back.setOnClickListener(view -> {
            finish();
        });
        checkVersion.setOnClickListener(view -> {
            Toast.makeText(this, "无更新", Toast.LENGTH_SHORT).show();
        });
    }
}
