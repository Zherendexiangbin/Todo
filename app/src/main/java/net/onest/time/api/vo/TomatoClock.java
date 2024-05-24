package net.onest.time.api.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class TomatoClock implements Serializable {
    private Long clockId;
    private Long taskId;
    private Integer sequence;
    private Integer clockDuration;
    private Integer clockStatus;
    private String stopReason;
    private Integer innerInterrupt;
    private Integer outerInterrupt;
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
        this.innerInterrupt = tomatoClockVo.getInnerInterrupt();
        this.outerInterrupt = tomatoClockVo.getOuterInterrupt();
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

    public Integer getInnerInterrupt() {
        return innerInterrupt;
    }

    public void setInnerInterrupt(Integer innerInterrupt) {
        this.innerInterrupt = innerInterrupt;
    }

    public Integer getOuterInterrupt() {
        return outerInterrupt;
    }

    public void setOuterInterrupt(Integer outerInterrupt) {
        this.outerInterrupt = outerInterrupt;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TomatoClock that = (TomatoClock) o;
        return Objects.equals(clockId, that.clockId) && Objects.equals(taskId, that.taskId) && Objects.equals(sequence, that.sequence) && Objects.equals(clockDuration, that.clockDuration) && Objects.equals(clockStatus, that.clockStatus) && Objects.equals(stopReason, that.stopReason) && Objects.equals(innerInterrupt, that.innerInterrupt) && Objects.equals(outerInterrupt, that.outerInterrupt) && Objects.equals(startedAt, that.startedAt) && Objects.equals(completedAt, that.completedAt) && Objects.equals(createdAt, that.createdAt) && Objects.equals(updatedAt, that.updatedAt) && Objects.equals(deleted, that.deleted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clockId, taskId, sequence, clockDuration, clockStatus, stopReason, innerInterrupt, outerInterrupt, startedAt, completedAt, createdAt, updatedAt, deleted);
    }

    @Override
    public String toString() {
        return "TomatoClock{" +
                "clockId=" + clockId +
                ", taskId=" + taskId +
                ", sequence=" + sequence +
                ", clockDuration=" + clockDuration +
                ", clockStatus=" + clockStatus +
                ", stopReason='" + stopReason + '\'' +
                ", innerInterrupt=" + innerInterrupt +
                ", outerInterrupt=" + outerInterrupt +
                ", startedAt=" + startedAt +
                ", completedAt=" + completedAt +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", deleted=" + deleted +
                '}';
    }
}