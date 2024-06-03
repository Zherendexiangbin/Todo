package net.onest.time.navigation.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import net.onest.time.R;

public class StudyRoomChatActivity extends AppCompatActivity {
    private TextView roomName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_room_chat_page);

        findView();
    }

    private void findView() {
        roomName = findViewById(R.id.room_name);
        roomName.setText(getIntent().getStringExtra("roomId"));
    }
}
