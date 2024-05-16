package net.onest.time.api.vo;

import java.io.Serializable;
import java.util.Date;

public class TomatoClock implements Serializable {
    private Long clockId;
    private Long taskId;
    private Integer sequence;
    private Integer clockStatus;
    private String stopReason;
    private Integer innerInterrupt;
    private Integer outerInterrupt;
    private Date startedAt;
    private Date completedAt;
    private Date createdAt;
    private Date updatedAt;
    private Integer deleted;

    public enum Status {
        COMPLETE(0), DOING(1), UN_STARTED(2), TERMINATED(3);
        private Integer status;

        Status(Integer status) {
            this.status = status;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public static Status of(Integer code) {
            switch (code) {
                case 0:
                    return Status.COMPLETE;
                case 1:
                    return Status.DOING;
                case 2:
                    return Status.UN_STARTED;
                case 3:
                    return Status.TERMINATED;
            }
            return null;
        }
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

    public Status getClockStatus() {
        return Status.of(clockStatus);
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

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", clockId=").append(clockId);
        sb.append(", taskId=").append(taskId);
        sb.append(", sequence=").append(sequence);
        sb.append(", clockStatus=").append(clockStatus);
        sb.append(", stopReason=").append(stopReason);
        sb.append(", innerInterrupt=").append(innerInterrupt);
        sb.append(", outerInterrupt=").append(outerInterrupt);
        sb.append(", startedAt=").append(startedAt);
        sb.append(", completedAt=").append(completedAt);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append(", deleted=").append(deleted);
        sb.append("]");
        return sb.toString();
    }
}