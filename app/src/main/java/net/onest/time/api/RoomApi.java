package net.onest.time.api;

import com.google.gson.reflect.TypeToken;

import net.onest.time.api.dto.RoomDto;
import net.onest.time.api.utils.RequestUtil;
import net.onest.time.api.vo.Page;
import net.onest.time.api.vo.RoomVo;
import net.onest.time.api.vo.UserVo;

import java.util.List;

public class RoomApi {
    private final static String PREFIX = "/room";

    // 创建自习室
    private final static String CREATE = "/create";

    // 生成邀请码
    private final static String GENERATE_INVITATION_CODE = "/generateInvitationCode";

    // 接受邀请
    private final static String ACCEPT_INVITATION = "/acceptInvitation";

    // 查询自习室中的所有用户
    private final static String USER_LIST = "/user/list";

    // 查询用户加入的所有自习室
    private final static String LIST_ROOM = "/list";

    // 管理员移除用户
    private final static String REMOVE_USER = "/user/remove";

    // 用户退出自习室
    private final static String USER_EXIT = "/user/exit";

    // 删除自习室
    private final static String DELETE_ROOM = "/";

    // 修改自习室
    private final static String UPDATE_ROOM = "/update";

    // 查询自习室
    private final static String FIND_ROOMS = "/";

    // 用户申请加入自习室
    private final static String REQUEST_JOIN = "/user/requestJoin";

    // 管理员同意用户加入自习室
    private final static String ACCEPT_REQUEST = "/manager/acceptRequest";

    // 管理员拒绝用户加入自习室
    private final static String REJECT_REQUEST = "/manager/rejectRequest";

    // 管理员查询所有的加入自习室的请求
    private final static String FIND_REQUESTS = "/manager/requests";

    // 查询用户加入的自习室
    private final static String GET_ROOM_INFO = "/getRoomInfo";


    public static RoomVo createRoom(RoomDto roomDto) {
        return RequestUtil.builder()
                .url(ServerConstant.HTTP_ADDRESS + PREFIX + CREATE)
                .post(roomDto)
                .buildAndSend(RoomVo.class);
    }

    public static String generateInvitationCode(Long roomId) {
        return RequestUtil.builder()
                .url(ServerConstant.HTTP_ADDRESS + PREFIX + GENERATE_INVITATION_CODE + "?roomId=" + roomId)
                .get()
                .buildAndSend(String.class);
    }

    public static void acceptInvitation(String invitationCode) {
        RequestUtil.builder()
            .url(ServerConstant.HTTP_ADDRESS + PREFIX + ACCEPT_INVITATION + "?invitationCode=" + invitationCode)
            .post()
            .buildAndSend();
    }

    public static List<UserVo> listUsers(Long roomId) {
        return RequestUtil.builder()
                .url(ServerConstant.HTTP_ADDRESS + PREFIX + USER_LIST + "?roomId=" + roomId)
                .get()
                .buildAndSend(new TypeToken<List<UserVo>>(){});
    }

    public static List<RoomVo> listRooms() {
       return RequestUtil.builder()
                .url(ServerConstant.HTTP_ADDRESS + PREFIX + LIST_ROOM)
                .get()
                .buildAndSend(new TypeToken<List<RoomVo>>(){});
    }

    public static void removeUser(Long roomId, Long userId){
        RequestUtil.builder()
                .url(ServerConstant.HTTP_ADDRESS + PREFIX + REMOVE_USER + "?roomId=" + roomId + "&userId=" + userId)
                .delete()
                .buildAndSend();
    }

    public static void userExit(Long roomId){
        RequestUtil.builder()
                .url(ServerConstant.HTTP_ADDRESS + PREFIX + USER_EXIT + "?roomId=" + roomId)
                .delete()
                .buildAndSend();
    }

    public static void deleteRoom(Long roomId){
        RequestUtil.builder()
                .url(ServerConstant.HTTP_ADDRESS + PREFIX + DELETE_ROOM + "?roomId=" + roomId)
                .delete()
                .buildAndSend();
    }

    public static void updateRoom(RoomDto roomDto) {
        RequestUtil.builder()
                .url(ServerConstant.HTTP_ADDRESS + PREFIX + UPDATE_ROOM)
                .put(roomDto)
                .buildAndSend();
    }

    public static Page<RoomVo> findRooms(RoomDto roomDto, int pageNum, int pageSize) {
        return RequestUtil.builder()
                    .url(ServerConstant.HTTP_ADDRESS + PREFIX + FIND_ROOMS + RequestUtil.parseParams(roomDto, "pageNum", pageNum, "pageSize", pageSize))
                    .get()
                    .buildAndSend(new TypeToken<Page<RoomVo>>(){});
    }

    public static void requestJoin(Long roomId){
        RequestUtil.builder()
                .url(ServerConstant.HTTP_ADDRESS + PREFIX + REQUEST_JOIN + "?roomId=" + roomId)
                .post()
                .buildAndSend();
    }

    public static void acceptRequest(Long roomId, Long userId) {
        RequestUtil.builder()
                .url(ServerConstant.HTTP_ADDRESS + PREFIX + ACCEPT_REQUEST + "?roomId=" + roomId + "&userId=" + userId)
                .post()
                .buildAndSend();
    }

    public static void rejectRequest(Long roomId, Long userId) {
        RequestUtil.builder()
                .url(ServerConstant.HTTP_ADDRESS + PREFIX + REJECT_REQUEST + "?roomId=" + roomId + "&userId=" + userId)
                .post()
                .buildAndSend();
    }

    public static List<UserVo> findRequests(Long roomId) {
        return RequestUtil.builder()
                .url(ServerConstant.HTTP_ADDRESS + PREFIX + FIND_REQUESTS + "?roomId=" + roomId)
                .get()
                .buildAndSend(new TypeToken<List<UserVo>>(){});
    }

    public static RoomVo getRoomInfo() {
        return RequestUtil.builder()
                .url(ServerConstant.HTTP_ADDRESS + PREFIX + GET_ROOM_INFO)
                .get()
                .buildAndSend(new TypeToken<RoomVo>(){});
    }
}
