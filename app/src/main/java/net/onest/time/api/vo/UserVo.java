package net.onest.time.api.vo;

public class UserVo {
    private Long userId;
    private String userName;
    private String avatar;
    private String signature;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    @Override
    public String toString() {
        return "UserVo{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", avatar='" + avatar + '\'' +
                ", signature='" + signature + '\'' +
                '}';
    }
}