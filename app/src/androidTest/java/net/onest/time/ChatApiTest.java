package net.onest.time;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import net.onest.time.api.ChatApi;
import net.onest.time.api.UserApi;
import net.onest.time.api.dto.MessageDto;
import net.onest.time.api.dto.UserDto;
import net.onest.time.api.utils.MessageListener;
import net.onest.time.api.vo.Message;
import net.onest.time.api.vo.MessageVo;
import net.onest.time.api.vo.Page;
import net.onest.time.application.TimeApplication;
import net.onest.time.constant.SharedPreferencesConstant;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class ChatApiTest {

    @Test
    public void login2() {
        UserDto userDto = new UserDto();
        userDto.setEmail("2808021998@qq.com");
        userDto.setPassword("admin");
        String token = UserApi.login(userDto);
        System.out.println(token);
    }

    @Test
    public void login18() {
        UserDto userDto = new UserDto();
        userDto.setEmail("1241250055@qq.com");
        userDto.setPassword("123456");
        String token = UserApi.login(userDto);
        System.out.println(token);
    }

    @Test
    public void sendRoomMessage() {
        login2();
        List<Message> messages = new CopyOnWriteArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                ChatApi.connectRoom(1782930828125134849L, new MessageListener(messages));
                MessageDto messageDto = new MessageDto();
                messageDto.setToRoomId(1782930828125134849L);
                messageDto.setContent("土狗日");
                ChatApi.sendMessage(messageDto);
            }
        }).start();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(messages);
    }

    @Test
    public void receiveRoomMessage() {
        login2();
        sendRoomMessage();
        login18();
        Long roomId = 1782930828125134849L;
        List<MessageVo> messages = ChatApi.receiveRoomMessage(roomId);
        System.out.println(messages);
    }

    @Test
    public void findRoomMessagePage() {
        Integer pageNum = 1;
        Integer pageSize = 20;
        Long roomId = 1782930828125134849L;
        Long beforeDateTime = 1715607032000L;
        Page<MessageVo> roomMessagePage = ChatApi.findRoomMessagePage(pageNum, pageSize, roomId, beforeDateTime);
        System.out.println(roomMessagePage);
    }


    @Test
    public void sendUserMessage() {
        login2();
        List<Message> messages = new CopyOnWriteArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                ChatApi.connectUser(18L, new MessageListener(messages));
                MessageDto messageDto = new MessageDto();
                messageDto.setToUserId(18L);
                messageDto.setContent("土狗日");
                ChatApi.sendMessage(messageDto);
            }
        }).start();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(messages);
    }

    @Test
    public void receiveUserMessage() {
        Long fromUserId = 2L;
        List<MessageVo> messages = ChatApi.receiveUserMessage(fromUserId);

    }

    @Test
    public void findUserMessagePage() {
        Integer pageNum = 1;
        Integer pageSize = 20;
        Long fromUserId = 2L;
        Long beforeDateTime = 1715607032000L;
        Page<MessageVo> userMessagePage = ChatApi.findUserMessagePage(pageNum, pageSize, fromUserId, beforeDateTime);
        Assert.assertNotNull(userMessagePage);
        Assert.assertFalse(userMessagePage.getRecords().isEmpty());
    }
}
