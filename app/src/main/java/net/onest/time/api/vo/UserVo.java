package net.onest.time.api.vo;

import java.util.Date;
import java.util.Objects;

public class UserVo {
    private Long userId;
    private String userName;
    private String avatar;
    private String signature;
    private Date createdAt;

    private Long tomatoDuration;

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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Long getTomatoDuration() {
        return tomatoDuration;
    }

    public void setTomatoDuration(Long tomatoDuration) {
        this.tomatoDuration = tomatoDuration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserVo userVo = (UserVo) o;
        return Objects.equals(userId, userVo.userId);
    }

    @Override
    public String toString() {
        return "UserVo{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", avatar='" + avatar + '\'' +
                ", signature='" + signature + '\'' +
                ", createdAt=" + createdAt +
                ", tomatoDuration=" + tomatoDuration +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

}