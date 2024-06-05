package net.onest.time.api.vo;

import java.io.Serializable;

public class RoomVo implements Serializable {
    private Long roomId;
    private Long userId;
    private String roomName;
    private String roomAvatar;

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomAvatar() {
        return roomAvatar;
    }

    public void setRoomAvatar(String roomAvatar) {
        this.roomAvatar = roomAvatar;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "RoomVo{" +
                "roomId=" + roomId +
                ", userId=" + userId +
                ", roomName='" + roomName + '\'' +
                ", roomAvatar='" + roomAvatar + '\'' +
                '}';
    }
}
