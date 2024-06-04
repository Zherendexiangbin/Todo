package net.onest.time.navigation.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.onest.time.R;
import net.onest.time.adapter.studyroom.ChatMsgAdapter;
import net.onest.time.api.UserApi;
import net.onest.time.api.vo.Message;
import net.onest.time.api.vo.UserVo;

import java.time.LocalDate;
import java.util.Date;
import java.util.LinkedList;

public class StudyRoomChatActivity extends AppCompatActivity {
    private UserVo userVo;
    private Button btnBack, btnSend;
    private TextView roomName;
    private LinkedList<Message> messagesList = new LinkedList<>();
    private ChatMsgAdapter chatMsgAdapter;
    private RecyclerView messagesView;
    private EditText editMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_room_chat_page);
        userVo = UserApi.getUserInfo();
        findView();
        initData();
        setListeners();
    }

    private void initData(){
        Message message1 = new Message();
        message1.setFromUserId(Long.parseLong("25"));
        message1.setContent("haha");
        message1.setSendTime(new Date(System.currentTimeMillis()));
        Message message2 = new Message();
        message2.setFromUserId(Long.parseLong("26"));
        message2.setContent("haha");
        message2.setSendTime(new Date(System.currentTimeMillis()));
        Message message3 = new Message();
        message3.setFromUserId(Long.parseLong("20"));
        message3.setContent("haha");
        message3.setSendTime(new Date(System.currentTimeMillis()));
        messagesList.add(message1);
        messagesList.add(message2);
        messagesList.add(message3);

        //绑定适配器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        messagesView.setLayoutManager(layoutManager);
        chatMsgAdapter = new ChatMsgAdapter(this, messagesList, userVo.getUserId());
        messagesView.setAdapter(chatMsgAdapter);
    }

    private void setListeners() {
        //返回自习室页面
        btnBack.setOnClickListener(view -> {finish();});

        //发送消息
        btnSend.setOnClickListener(view -> {
            if (editMessage.getText().toString().isEmpty()){
                Toast.makeText(this, "发送消息不可为空", Toast.LENGTH_SHORT).show();
            }else {
                Message message = new Message();
                message.setFromUserId(userVo.getUserId());
                message.setContent(editMessage.getText().toString());
                message.setSendTime(new Date(System.currentTimeMillis()));
                messagesList.add(message);
                chatMsgAdapter.updateData(messagesList);
                editMessage.setText("");
            }
        });
    }

    private void findView() {
        btnBack = findViewById(R.id.btn_back);
        roomName = findViewById(R.id.room_name);
        roomName.setText(getIntent().getStringExtra("roomId"));

        messagesView = findViewById(R.id.chat_message);
        editMessage = findViewById(R.id.chat_edit);
        btnSend = findViewById(R.id.btn_send);
    }
}
