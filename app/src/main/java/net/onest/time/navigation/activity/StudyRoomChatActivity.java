package net.onest.time.navigation.activity;

import android.graphics.Rect;
import android.icu.text.Transliterator;
import android.os.Bundle;
import android.system.SystemCleaner;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import net.onest.time.R;
import net.onest.time.adapter.studyroom.ChatMsgAdapter;
import net.onest.time.api.ChatApi;
import net.onest.time.api.RoomApi;
import net.onest.time.api.UserApi;
import net.onest.time.api.vo.Message;
import net.onest.time.api.vo.MessageVo;
import net.onest.time.api.vo.Page;
import net.onest.time.api.vo.RoomVo;
import net.onest.time.api.vo.UserVo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class StudyRoomChatActivity extends AppCompatActivity {
    private UserVo userVo;
    private RoomVo roomVo;
    private Button btnBack, btnSend;
    private TextView roomName;
    private Page<MessageVo> historyMessagesList = new Page<>();
    private int pageNum = 1;
    private List<MessageVo> messagesList = new ArrayList<>();
    private ChatMsgAdapter chatMsgAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView messagesView;
    private EditText editMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_room_chat_page);
        userVo = UserApi.getUserInfo();
        roomVo = RoomApi.getRoomInfo();
        findView();
        initData();
        setListeners();
        setKeyboardListener();
    }

    private void initData(){

        historyMessagesList = ChatApi.findRoomMessagePage(1, 10, roomVo.getRoomId(), System.currentTimeMillis());
        messagesList = historyMessagesList.getRecords();
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
                MessageVo message = new MessageVo();
                message.setFromUserId(userVo.getUserId());
                message.setContent(editMessage.getText().toString());
                message.setSendTime(new Date(System.currentTimeMillis()));
                messagesList.add(message);
                chatMsgAdapter.updateData(messagesList);
                editMessage.setText("");
            }
        });

        //下拉刷新
        swipeRefreshLayout.setOnRefreshListener(() -> {
            pageNum += 1;
            getMessages(Position.FIRST, false);
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    private void findView() {
        btnBack = findViewById(R.id.btn_back);
        roomName = findViewById(R.id.room_name);
        roomName.setText(roomVo.getRoomName());

        swipeRefreshLayout = findViewById(R.id.swipRefresher);
        messagesView = findViewById(R.id.chat_message);
        editMessage = findViewById(R.id.chat_edit);
        btnSend = findViewById(R.id.btn_send);
    }

    //下拉刷新获取消息
    private void getMessages(Position position, boolean scrollToEnd){
        try {
            Page<MessageVo> newMessages = ChatApi.findRoomMessagePage(pageNum, 10, roomVo.getRoomId(), messagesList.get(0).getSendTime().getTime());
            if (newMessages != null) {
                newMessages.getRecords()
                        .forEach(messageVo -> addAndRefreshMsgList(messageVo, position, scrollToEnd));
                messagesList = newMessages.getRecords();
                chatMsgAdapter.updateData(messagesList);
            }else {
                Toast.makeText(this, "没有其他消息了", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){

        }
    }

    /**
     * @param messageVo     将要插入的消息
     * @param position    插入的位置
     * @param scrollToEnd 是否滚动到最好一条消息
     */
    private void addAndRefreshMsgList(MessageVo messageVo, Position position, boolean scrollToEnd) {
        runOnUiThread(() -> {
            if (position == Position.FIRST) {
                messagesList.add(0, messageVo);
                //当有新消息时，刷新RecyclerView中的显示
                chatMsgAdapter.notifyItemInserted(0);
            } else if (position == Position.LAST) {
                messagesList.add(messageVo);
                //当有新消息时，刷新RecyclerView中的显示
                chatMsgAdapter.notifyItemInserted(messagesList.size() - 1);
            }
            if (scrollToEnd) {
                //将RecyclerView定位到最后一行
                messagesView.scrollToPosition(messagesList.size() - 1);
            }
        });
    }

    private enum Position {
        FIRST,
        LAST
    }

    //监听键盘是否弹出
    private void setKeyboardListener(){
        View contentView = findViewById(android.R.id.content);
        contentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                contentView.getWindowVisibleDisplayFrame(rect);
                int screenHeight = contentView.getRootView().getHeight();
                int keypadHeight = screenHeight - rect.bottom;
                if (keypadHeight > screenHeight * 0.15){
                    //键盘弹出
                    messagesView.scrollToPosition(messagesList.size()-1);
                }
            }
        });
    }
}
