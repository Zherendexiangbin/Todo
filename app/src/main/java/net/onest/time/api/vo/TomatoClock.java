package net.onest.time.api.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class TomatoClock implements Serializable {
    private Long clockId;
    private Long taskId;
    private Long parentId;
    private Integer sequence;
    private Integer clockDuration;
    private Integer clockStatus;
    private String stopReason;
    private Date startedAt;
    private Date completedAt;
    private Date createdAt;
    private Date updatedAt;
    private Integer deleted;

    public TomatoClock withTomatoClockVo(TomatoClockVo tomatoClockVo) {
        this.clockId = tomatoClockVo.getClockId();
        this.taskId = tomatoClockVo.getTaskId();
        this.sequence = tomatoClockVo.getSequence();
        this.clockDuration = tomatoClockVo.getClockDuration();
        this.clockStatus = tomatoClockVo.getClockStatus().getStatus();
        this.stopReason = tomatoClockVo.getStopReason();
        this.startedAt = tomatoClockVo.getStartedAt();
        this.completedAt = tomatoClockVo.getCompletedAt();
        return this;
    }

    public Long getClockId() {
        return clockId;
    }

    public void setClockId(Long clockId) {
        this.clockId = clockId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }



    public void setClockStatus(Integer clockStatus) {
        this.clockStatus = clockStatus;
    }

    public String getStopReason() {
        return stopReason;
    }

    public void setStopReason(String stopReason) {
        this.stopReason = stopReason;
    }

    public Date getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Date startedAt) {
        this.startedAt = startedAt;
    }

    public Date getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Date completedAt) {
        this.completedAt = completedAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public Integer getClockDuration() {
        return clockDuration;
    }

    public void setClockDuration(Integer clockDuration) {
        this.clockDuration = clockDuration;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getClockStatus() {
        return clockStatus;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TomatoClock that = (TomatoClock) o;
        return Objects.equals(clockId, that.clockId) && Objects.equals(taskId, that.taskId) && Objects.equals(parentId, that.parentId) && Objects.equals(sequence, that.sequence) && Objects.equals(clockDuration, that.clockDuration) && Objects.equals(clockStatus, that.clockStatus) && Objects.equals(stopReason, that.stopReason) && Objects.equals(startedAt, that.startedAt) && Objects.equals(completedAt, that.completedAt) && Objects.equals(createdAt, that.createdAt) && Objects.equals(updatedAt, that.updatedAt) && Objects.equals(deleted, that.deleted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clockId, taskId, parentId, sequence, clockDuration, clockStatus, stopReason, startedAt, completedAt, createdAt, updatedAt, deleted);
    }


    @Override
    public String toString() {
        return "TomatoClock{" +
                "clockId=" + clockId +
                ", taskId=" + taskId +
                ", parentId=" + parentId +
                ", sequence=" + sequence +
                ", clockDuration=" + clockDuration +
                ", clockStatus=" + clockStatus +
                ", stopReason='" + stopReason + '\'' +
                ", startedAt=" + startedAt +
                ", completedAt=" + completedAt +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", deleted=" + deleted +
                '}';
    }

}