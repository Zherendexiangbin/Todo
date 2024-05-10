package net.onest.time.api.vo;

public class RoomVo {
    private Long roomId;
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

    @Override
    public String toString() {
        return "RoomVo{" +
                "roomId=" + roomId +
                ", roomName='" + roomName + '\'' +
                ", roomAvatar='" + roomAvatar + '\'' +
                '}';
    }
}
