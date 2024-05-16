package net.onest.time.api.vo.statistic;

public class TaskRatio {
    private String taskName;                    // 任务名
    private Double ratio;                       // 比例

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Double getRatio() {
        return ratio;
    }

    public void setRatio(Double ratio) {
        this.ratio = ratio;
    }

    @Override
    public String toString() {
        return "TaskRatio{" +
                "taskName='" + taskName + '\'' +
                ", ratio=" + ratio +
                '}';
    }
}
