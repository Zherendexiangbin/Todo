package net.onest.time.api.dto;

public class MessageDto {
    private Long fromUserId;
    private Long toUserId;
    private Long toRoomId;
    private String content;

    private Integer type;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(Long fromUserId) {
        this.fromUserId = fromUserId;
    }

    public Long getToUserId() {
        return toUserId;
    }

    public void setToUserId(Long toUserId) {
        this.toUserId = toUserId;
    }

    public Long getToRoomId() {
        return toRoomId;
    }

    public void setToRoomId(Long toRoomId) {
        this.toRoomId = toRoomId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "MessageDto{" +
                "fromUserId=" + fromUserId +
                ", toUserId=" + toUserId +
                ", toRoomId=" + toRoomId +
                ", content='" + content + '\'' +
                ", type=" + type +
                '}';
    }
}
