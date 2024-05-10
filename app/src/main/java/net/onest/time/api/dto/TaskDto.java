package net.onest.time.api.dto;

import net.onest.time.api.vo.TomatoClock;

import java.util.Date;
import java.util.List;

public class TaskDto {
    private Long taskId;
    private Long userId;

    private String taskName;

    private List<Integer> estimate;

    private Integer tomatoClockTimes;
    private List<TomatoClock> tomatoClocks;

    private Integer stopTimes;

    private Integer taskStatus;

    private Integer innerInterrupt;
    private Integer outerInterrupt;

    private Date startedAt;
    private Date completedAt;

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public List<Integer> getEstimate() {
        return estimate;
    }

    public void setEstimate(List<Integer> estimate) {
        this.estimate = estimate;
    }

    public Integer getTomatoClockTimes() {
        return tomatoClockTimes;
    }

    public void setTomatoClockTimes(Integer tomatoClockTimes) {
        this.tomatoClockTimes = tomatoClockTimes;
    }

    public List<TomatoClock> getTomatoClocks() {
        return tomatoClocks;
    }

    public void setTomatoClocks(List<TomatoClock> tomatoClocks) {
        this.tomatoClocks = tomatoClocks;
    }

    public Integer getStopTimes() {
        return stopTimes;
    }

    public void setStopTimes(Integer stopTimes) {
        this.stopTimes = stopTimes;
    }

    public Integer getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(Integer taskStatus) {
        this.taskStatus = taskStatus;
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

    @Override
    public String toString() {
        return "TaskDto{" +
                "taskId=" + taskId +
                ", userId=" + userId +
                ", taskName='" + taskName + '\'' +
                ", estimate=" + estimate +
                ", tomatoClockTimes=" + tomatoClockTimes +
                ", tomatoClocks=" + tomatoClocks +
                ", stopTimes=" + stopTimes +
                ", taskStatus=" + taskStatus +
                ", innerInterrupt=" + innerInterrupt +
                ", outerInterrupt=" + outerInterrupt +
                ", startedAt=" + startedAt +
                ", completedAt=" + completedAt +
                '}';
    }
}
