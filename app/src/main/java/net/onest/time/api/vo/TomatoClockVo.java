package net.onest.time.api.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class TomatoClockVo implements Serializable {
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

    public Integer getClockDuration() {
        return clockDuration;
    }

    public void setClockDuration(Integer clockDuration) {
        this.clockDuration = clockDuration;
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
        return of(clockStatus);
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TomatoClockVo that = (TomatoClockVo) o;
        return Objects.equals(clockId, that.clockId) && Objects.equals(taskId, that.taskId) && Objects.equals(sequence, that.sequence) && Objects.equals(clockDuration, that.clockDuration) && Objects.equals(clockStatus, that.clockStatus) && Objects.equals(stopReason, that.stopReason) && Objects.equals(innerInterrupt, that.innerInterrupt) && Objects.equals(outerInterrupt, that.outerInterrupt) && Objects.equals(startedAt, that.startedAt) && Objects.equals(completedAt, that.completedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clockId, taskId, sequence, clockDuration, clockStatus, stopReason, innerInterrupt, outerInterrupt, startedAt, completedAt);
    }

    @Override
    public String toString() {
        return "TomatoClockVo{" +
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
                '}';
    }

    public static TomatoClockVo.Status of(Integer code) {
        switch (code) {
            case 0:
                return TomatoClockVo.Status.COMPLETE;
            case 1:
                return TomatoClockVo.Status.DOING;
            case 2:
                return TomatoClockVo.Status.UN_STARTED;
            case 3:
                return TomatoClockVo.Status.TERMINATED;
        }
        return null;
    }

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

    }
}
