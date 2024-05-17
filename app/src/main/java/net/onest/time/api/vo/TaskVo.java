package net.onest.time.api.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class TaskVo implements Serializable {
    private Long taskId;
    private Long userId;
    private String taskName;
    private Integer taskStatus;
    private Integer clockDuration;
    private String remark;
    private List<Integer> estimate;
    private Integer restTime;
    private Integer again;

    private String category;
    private Integer tomatoClockTimes;
    private List<TomatoClockVo> tomatoClocks;
    private Integer stopTimes;

    private String background;
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

    public Integer getClockDuration() {
        return clockDuration;
    }

    public void setClockDuration(Integer clockDuration) {
        this.clockDuration = clockDuration;
    }

    public Integer getTomatoClockTimes() {
        return tomatoClockTimes;
    }

    public void setTomatoClockTimes(Integer tomatoClockTimes) {
        this.tomatoClockTimes = tomatoClockTimes;
    }

    public List<TomatoClockVo> getTomatoClocks() {
        return tomatoClocks;
    }

    public void setTomatoClocks(List<TomatoClockVo> tomatoClocks) {
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

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getRestTime() {
        return restTime;
    }

    public void setRestTime(Integer restTime) {
        this.restTime = restTime;
    }

    public Integer getAgain() {
        return again;
    }

    public void setAgain(Integer again) {
        this.again = again;
    }

    @Override
    public String toString() {
        return "TaskVo{" +
                "taskId=" + taskId +
                ", userId=" + userId +
                ", taskName='" + taskName + '\'' +
                ", clockDuration=" + clockDuration +
                ", taskStatus=" + taskStatus +
                ", remark='" + remark + '\'' +
                ", estimate=" + estimate +
                ", restTime=" + restTime +
                ", again=" + again +
                ", category='" + category + '\'' +
                ", tomatoClockTimes=" + tomatoClockTimes +
                ", tomatoClocks=" + tomatoClocks +
                ", stopTimes=" + stopTimes +
                ", background='" + background + '\'' +
                ", innerInterrupt=" + innerInterrupt +
                ", outerInterrupt=" + outerInterrupt +
                ", startedAt=" + startedAt +
                ", completedAt=" + completedAt +
                '}';
    }
}
