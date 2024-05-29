package net.onest.time;

import net.onest.time.api.RoomApi;
import net.onest.time.api.dto.RoomDto;
import net.onest.time.api.vo.RoomVo;
import net.onest.time.api.vo.UserVo;

import org.junit.Test;

import java.util.List;

public class RoomApiTest {
    @Test
    public void createRoom() {
        RoomDto roomDto = new RoomDto();
        roomDto.setRoomName("土狗自习室");
        roomDto.setRoomAvatar("http://8.130.17.7:9000/todo-bucket/background/406.jpg");
        RoomVo room = RoomApi.createRoom(roomDto);
        System.out.println(room);
    }

    @Test
    public void generateInvitationCode() {
        Long roomId = 1789951868917055489L;
        String invitationCode = RoomApi.generateInvitationCode(roomId);
        System.out.println(invitationCode);
    }

    @Test
    public void requestJoin() {
        Long roomId = 1782930102082654209L;
        RoomApi.requestJoin(roomId);
    }

    @Test
    public void acceptInvitation() {
        String invitationCode = "796411";
        RoomApi.acceptInvitation(invitationCode);
    }

    @Test
    public void listUsers() {
        Long roomId = 1782930102082654209L;
        List<UserVo> userVoList = RoomApi.listUsers(roomId);
        System.out.println(userVoList);
    }

    @Test
    public void listRooms() {
        List<RoomVo> roomVoList = RoomApi.listRooms();
        System.out.println(roomVoList);
    }

    @Test
    public void removeUser() {
        Long roomId = 1789951868917055489L;
        RoomApi.removeUser(roomId, 2L);
    }

    @Test
    public void acceptRequest() {
        Long roomId = 1789951868917055489L;
        RoomApi.acceptRequest(roomId, 18L);
    }

    @Test
    public void findRequests() {
        Long roomId = 1789951868917055489L;
        List<UserVo> requests = RoomApi.findRequests(roomId);
        System.out.println(requests);
    }

    @Test
    public void userExit() {
        Long roomId = 1782930102082654209L;
        RoomApi.userExit(roomId);
    }

    @Test
    public void updateRoom() {
        RoomDto roomDto = new RoomDto();
        roomDto.setRoomId(1789951868917055489L);
        roomDto.setRoomName("土狗滴爱，那么深情");
        roomDto.setRoomAvatar("http://8.130.17.7:9000/todo-bucket/1.jpeg");
        RoomApi.updateRoom(roomDto);
    }

    @Test
    public void deleteRoom() {
        Long roomId = 1789951868917055489L;
        RoomApi.deleteRoom(roomId);
    }

    @Test
    public void getRoomInfo() {
        RoomVo roomInfo = RoomApi.getRoomInfo();
        System.out.println(roomInfo);
    }
}
