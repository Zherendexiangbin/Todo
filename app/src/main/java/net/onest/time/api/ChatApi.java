package net.onest.time.api;

import com.google.gson.reflect.TypeToken;

import net.onest.time.api.dto.MessageDto;
import net.onest.time.api.utils.MessageListener;
import net.onest.time.api.utils.RequestUtil;
import net.onest.time.api.vo.Message;
import net.onest.time.api.vo.Page;

import java.util.List;

public class ChatApi {
    private final static String CHAT_ROOM_PREFIX = "/chat/room";
    private final static String CHAT_USER_PREFIX = "/chat/user";

    // 接收消息
    private final static String RECEIVE_MESSAGE = "/receive";

    // 分页查询消息
    private final static String FIND_MESSAGE_PAGE = "/";
    
    private final static String CONNECT_ROOM = "/roomchat";
    private final static String CONNECT_USER = "/roomuser";

    public static List<Message> receiveRoomMessage(Long roomId) {
        return RequestUtil.builder()
                .url(ServerConstant.HTTP_ADDRESS + CHAT_ROOM_PREFIX + RECEIVE_MESSAGE + "?roomId=" + roomId)
                .get()
                .buildAndSend(new TypeToken<List<Message>>(){});
    }

    public static Page<Message> findRoomMessagePage(Integer pageNum, Integer pageSize, Long roomId, Long beforeDateTime) {
        return RequestUtil.builder()
                .url(ServerConstant.HTTP_ADDRESS + CHAT_ROOM_PREFIX + FIND_MESSAGE_PAGE + roomId + "/" + pageNum + "/" + pageSize + "?beforeDateTime=" + beforeDateTime)
                .get()
                .buildAndSend(new TypeToken<Page<Message>>(){});
    }

    public static List<Message> receiveUserMessage(Long fromUserId) {
        return RequestUtil.builder()
                .url(ServerConstant.HTTP_ADDRESS + CHAT_USER_PREFIX + RECEIVE_MESSAGE + "?fromUserId=" + fromUserId)
                .get()
                .buildAndSend(new TypeToken<List<Message>>(){});
    }

    public static Page<Message> findUserMessagePage(Integer pageNum, Integer pageSize, Long userId, Long beforeDateTime) {
        return RequestUtil.builder()
                .url(ServerConstant.HTTP_ADDRESS + CHAT_USER_PREFIX + FIND_MESSAGE_PAGE + userId + "/" + pageNum + "/" + pageSize + "?beforeDateTime=" + beforeDateTime)
                .get()
                .buildAndSend(new TypeToken<Page<Message>>(){});
    }
    
    public static void connectRoom(Long toRoomId, MessageListener messageListener){
        RequestUtil.webSocketConnect(ServerConstant.WEBSOCKET_ADDRESS + CONNECT_ROOM + "/" + toRoomId, messageListener);
    }

    public static void connectUser(Long toUserId, MessageListener messageListener){
        RequestUtil.webSocketConnect(ServerConstant.WEBSOCKET_ADDRESS + CONNECT_USER + "/" + toUserId, messageListener);
    }

    public static void sendMessage(MessageDto messageDto){
        RequestUtil.sendMessage(messageDto);
    }

}
