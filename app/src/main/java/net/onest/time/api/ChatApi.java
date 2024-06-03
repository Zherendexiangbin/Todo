package net.onest.time.api;

import com.google.gson.reflect.TypeToken;

import net.onest.time.api.dto.MessageDto;
import net.onest.time.api.utils.MessageListener;
import net.onest.time.api.utils.RequestUtil;
import net.onest.time.api.vo.Message;
import net.onest.time.api.vo.MessageVo;
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

    public static List<MessageVo> receiveRoomMessage(Long roomId) {
        return RequestUtil.builder()
                .url(ServerConstant.HTTP_ADDRESS + CHAT_ROOM_PREFIX + RECEIVE_MESSAGE + "?roomId=" + roomId)
                .get()
                .buildAndSend(new TypeToken<List<MessageVo>>() {
                });
    }

    public static Page<MessageVo> findRoomMessagePage(Integer pageNum, Integer pageSize, Long roomId, Long beforeDateTime) {
        return RequestUtil.builder()
                .url(ServerConstant.HTTP_ADDRESS + CHAT_ROOM_PREFIX + FIND_MESSAGE_PAGE + roomId + "/" + pageNum + "/" + pageSize + "?beforeDateTime=" + beforeDateTime)
                .get()
                .buildAndSend(new TypeToken<Page<MessageVo>>() {
                });
    }

    public static List<MessageVo> receiveUserMessage(Long fromUserId) {
        return RequestUtil.builder()
                .url(ServerConstant.HTTP_ADDRESS + CHAT_USER_PREFIX + RECEIVE_MESSAGE + "?fromUserId=" + fromUserId)
                .get()
                .buildAndSend(new TypeToken<List<MessageVo>>() {
                });
    }

    public static Page<MessageVo> findUserMessagePage(Integer pageNum, Integer pageSize, Long userId, Long beforeDateTime) {
        return RequestUtil.builder()
                .url(ServerConstant.HTTP_ADDRESS + CHAT_USER_PREFIX + FIND_MESSAGE_PAGE + userId + "/" + pageNum + "/" + pageSize + "?beforeDateTime=" + beforeDateTime)
                .get()
                .buildAndSend(new TypeToken<Page<MessageVo>>() {
                });
    }

    /**
     * 创建和Room的Websocket连接
     * @param toRoomId 连接的roomId
     * @param messageListener 消息监听器
     */
    public static void connectRoom(Long toRoomId, MessageListener messageListener) {
        RequestUtil.webSocketConnect(ServerConstant.WEBSOCKET_ADDRESS + CONNECT_ROOM + "/" + toRoomId, messageListener);
    }

    /**
     * 创建和User的Websocket连接
     * @param toUserId 连接的userId
     * @param messageListener 消息监听器
     */
    public static void connectUser(Long toUserId, MessageListener messageListener) {
        RequestUtil.webSocketConnect(ServerConstant.WEBSOCKET_ADDRESS + CONNECT_USER + "/" + toUserId, messageListener);
    }

    /**
     * <p>在当前的会话中发送消息，调用该方法前，应确保调用过连接方法</p>
     * <p>{@link ChatApi#connectRoom(Long, MessageListener)}、{@link ChatApi#connectUser(Long, MessageListener)}</p>
     * @param messageDto 消息的传输对象
     */
    public static void sendMessage(MessageDto messageDto) {
        RequestUtil.sendMessage(messageDto);
    }
}
