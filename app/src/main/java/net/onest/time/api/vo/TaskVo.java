package net.onest.time.api.vo;

import java.util.Date;
import java.util.List;

public class TaskVo {
    private Long taskId;
    private Long userId;

    private String taskName;
    private List<Integer> estimate;

    private Integer tomatoClockTimes;
    private List<TomatoClock> tomatoClocks;
    private Integer stopTimes;

    private Integer taskStatus;
    private String background;
    private Integer innerInterrupt;
    private Integer outerInterrupt;

    private Date startedAt;
    private Date completedAt;

    @Override
    public String toString() {
        return "TaskVo{" +
                "taskId=" + taskId +
                ", userId=" + userId +
                ", taskName='" + taskName + '\'' +
                ", estimate=" + estimate +
                ", tomatoClockTimes=" + tomatoClockTimes +
                ", tomatoClocks=" + tomatoClocks +
                ", stopTimes=" + stopTimes +
                ", taskStatus=" + taskStatus +
                ", background='" + background + '\'' +
                ", innerInterrupt=" + innerInterrupt +
                ", outerInterrupt=" + outerInterrupt +
                ", startedAt=" + startedAt +
                ", completedAt=" + completedAt +
                '}';
    }
}
