package net.onest.time.navigation.activity;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import net.onest.time.R;
import net.onest.time.adapter.studyroom.ChatMsgAdapter;
import net.onest.time.api.ChatApi;
import net.onest.time.api.StatisticApi;
import net.onest.time.api.dto.MessageDto;
import net.onest.time.api.utils.MessageListener;
import net.onest.time.api.utils.RequestUtil;
import net.onest.time.api.vo.MessageVo;
import net.onest.time.api.vo.Page;
import net.onest.time.api.vo.RoomVo;
import net.onest.time.api.vo.UserVo;
import net.onest.time.api.vo.statistic.StatisticVo;
import net.onest.time.components.CheckInDialog;
import net.onest.time.constant.SharedPreferencesConstant;
import net.onest.time.constant.UserInfoConstant;
import net.onest.time.entity.CheckIn;

import java.util.ArrayList;
import java.util.List;

import okhttp3.WebSocket;

public class StudyRoomChatActivity extends AppCompatActivity {
    private UserVo userVo;
    private RoomVo roomVo;
    private Button btnBack, btnSend;
    private TextView roomName;
    private MessageListener messageListener;
    private Page<MessageVo> historyMessagesList = new Page<>();
    private int pageNum = 1;
    private List<MessageVo> messagesList = new ArrayList<>();
    private ChatMsgAdapter chatMsgAdapter;
    private RecyclerView messagesView;
    private EditText editMessage;
    private SmartRefreshLayout smartRefreshLayout;
    private Button checkIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_room_chat_page);

        findView();
        initData();
        ChatApi.connectRoom(roomVo.getRoomId(), messageListener);
        setListeners();
        setKeyboardListener();

        // 设置自习室名字
        roomName.setText(roomVo.getRoomName());

        // 连接
        ChatApi.connectRoom(
                roomVo.getRoomId(),
                new MessageListener(messagesList) {
                    @Override
                    public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {
                        super.onMessage(webSocket, text);
                        runOnUiThread(() -> {
                            chatMsgAdapter.notifyItemInserted(messagesList.size());
                            messagesView.smoothScrollToPosition(RecyclerView.SCROLL_INDICATOR_BOTTOM);
                        });
                    }
                }
        );
    }

    private void initData() {
        // 获取用户信息
        String userInfoJson = getSharedPreferences(SharedPreferencesConstant.USER_INFO, MODE_PRIVATE)
                .getString(UserInfoConstant.USER_INFO, "");
        userVo = RequestUtil.getGson().fromJson(userInfoJson, UserVo.class);

        // 获取自习室信息
        roomVo = (RoomVo) getIntent().getSerializableExtra("room");

        try {
            historyMessagesList = ChatApi.findRoomMessagePage(1, 10, roomVo.getRoomId(), System.currentTimeMillis());
            if (historyMessagesList != null) {
                messagesList = historyMessagesList.getRecords();
            } else {
                messagesList = new ArrayList<>();
            }
        } catch (Exception e) {

        }
        messageListener = new MessageListener(messagesList);
        // 绑定适配器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        messagesView.setLayoutManager(layoutManager);
        chatMsgAdapter = new ChatMsgAdapter(this, messagesList, userVo.getUserId());
        messagesView.setAdapter(chatMsgAdapter);
    }

    private void setListeners() {
        // 打卡按钮
        checkIn.setOnClickListener(v -> {
            StatisticVo statistic = StatisticApi.statistic();
            CheckIn checkInData = new CheckIn(
                    userVo.getUserName(),
                    statistic.getTomatoTimes(),
                    statistic.getTomatoDuration(),
                    statistic.getRatioByDurationOfDay()
            );
            new CheckInDialog(this, checkInData);
        });

        // 返回自习室页面
        btnBack.setOnClickListener(view -> {
            finish();
        });

        // 发送消息
        btnSend.setOnClickListener(view -> {
            if (editMessage.getText().toString().isEmpty()) {
                Toast.makeText(this, "发送消息不可为空", Toast.LENGTH_SHORT).show();
            } else {
                MessageDto messageDto = new MessageDto();
                messageDto.setToRoomId(roomVo.getRoomId());
                messageDto.setContent(editMessage.getText().toString());

                ChatApi.sendMessage(messageDto);

                editMessage.setText("");
            }
        });

        smartRefreshLayout.setOnRefreshListener(refreshLayout -> {
            pageNum += 1;
            getMessages(Position.FIRST, false);
            refreshLayout.finishRefresh(500, true, false);
        });
    }

    private void findView() {
        btnBack = findViewById(R.id.btn_back);
        roomName = findViewById(R.id.room_name);

        smartRefreshLayout = findViewById(R.id.smartRefresher);
        messagesView = findViewById(R.id.chat_message);
        editMessage = findViewById(R.id.chat_edit);
        btnSend = findViewById(R.id.btn_send);

        checkIn = findViewById(R.id.check_in);
    }

    // 下拉刷新获取消息
    private void getMessages(Position position, boolean scrollToEnd) {
        try {
            Page<MessageVo> newMessages = ChatApi.findRoomMessagePage(pageNum, 10, roomVo.getRoomId(), System.currentTimeMillis());
            if (newMessages != null) {
                List<MessageVo> messages = newMessages.getRecords();
                for (int i = messages.size() - 1; i >= 0; i--) {
                    addAndRefreshMsgList(messages.get(i), position, scrollToEnd);
                }
            } else {
                Toast.makeText(this, "没有其他消息了", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {

        }
    }

    /**
     * @param messageVo   将要插入的消息
     * @param position    插入的位置
     * @param scrollToEnd 是否滚动到最后一条消息
     */
    private void addAndRefreshMsgList(MessageVo messageVo, Position position, boolean scrollToEnd) {
        runOnUiThread(() -> {
            if (position == Position.FIRST) {
                messagesList.add(0, messageVo);
                // 当有新消息时，刷新RecyclerView中的显示
                chatMsgAdapter.notifyItemInserted(0);
            } else if (position == Position.LAST) {
                messagesList.add(messageVo);
                // 当有新消息时，刷新RecyclerView中的显示
                chatMsgAdapter.notifyItemInserted(messagesList.size() - 1);
            }
            if (scrollToEnd) {
                // 将RecyclerView定位到最后一行
                messagesView.scrollToPosition(messagesList.size() - 1);
            }
        });
    }

    private enum Position {
        FIRST,
        LAST
    }

    // 监听键盘是否弹出
    private void setKeyboardListener() {
        View contentView = findViewById(android.R.id.content);
        contentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                contentView.getWindowVisibleDisplayFrame(rect);
                int screenHeight = contentView.getRootView().getHeight();
                int keypadHeight = screenHeight - rect.bottom;
                if (keypadHeight > screenHeight * 0.15) {
                    // 键盘弹出
                    messagesView.scrollToPosition(messagesList.size() - 1);
                }
            }
        });
    }
}
