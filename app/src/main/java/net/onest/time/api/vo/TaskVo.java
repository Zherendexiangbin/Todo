package net.onest.time.api.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class TaskVo implements Serializable {
    private Long taskId;
    private Long parentId;
    private Long userId;
    private String taskName;
    private Integer type;
    private Integer taskStatus;
    private Integer clockDuration;
    private String remark;
    private List<Integer> estimate;
    private Integer restTime;
    private Integer again;
    private Long categoryId;
    private String background;
    private Date startedAt;
    private Date completedAt;
    private Date createdAt;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskVo taskVo = (TaskVo) o;
        return Objects.equals(taskId, taskVo.taskId) && Objects.equals(parentId, taskVo.parentId) && Objects.equals(userId, taskVo.userId) && Objects.equals(taskName, taskVo.taskName) && Objects.equals(type, taskVo.type) && Objects.equals(taskStatus, taskVo.taskStatus) && Objects.equals(clockDuration, taskVo.clockDuration) && Objects.equals(remark, taskVo.remark) && Objects.equals(estimate, taskVo.estimate) && Objects.equals(restTime, taskVo.restTime) && Objects.equals(again, taskVo.again) && Objects.equals(categoryId, taskVo.categoryId) && Objects.equals(background, taskVo.background) && Objects.equals(startedAt, taskVo.startedAt) && Objects.equals(completedAt, taskVo.completedAt) && Objects.equals(createdAt, taskVo.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskId, parentId, userId, taskName, type, taskStatus, clockDuration, remark, estimate, restTime, again, categoryId, background, startedAt, completedAt, createdAt);
    }

    @Override
    public String toString() {
        return "TaskVo{" +
                "taskId=" + taskId +
                ", parentId=" + parentId +
                ", userId=" + userId +
                ", taskName='" + taskName + '\'' +
                ", type=" + type +
                ", taskStatus=" + taskStatus +
                ", clockDuration=" + clockDuration +
                ", remark='" + remark + '\'' +
                ", estimate=" + estimate +
                ", restTime=" + restTime +
                ", again=" + again +
                ", categoryId=" + categoryId +
                ", background='" + background + '\'' +
                ", startedAt=" + startedAt +
                ", completedAt=" + completedAt +
                ", createdAt=" + createdAt +
                '}';
    }

}
