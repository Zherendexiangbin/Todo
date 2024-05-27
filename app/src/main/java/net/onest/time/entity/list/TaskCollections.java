package net.onest.time.entity.list;

import net.onest.time.api.vo.TaskVo;

import java.util.List;

public class TaskCollections {
    private String taskCollectionsName;
    private int taskCollectionsColor;
    private List<TaskVo> tasks;

    public TaskCollections() {
    }

    public TaskCollections(String taskCollectionsName, int taskCollectionsColor, List<TaskVo> tasks) {
        this.taskCollectionsName = taskCollectionsName;
        this.taskCollectionsColor = taskCollectionsColor;
        this.tasks = tasks;
    }

    public String getTaskCollectionsName() {
        return taskCollectionsName;
    }

    public void setTaskCollectionsName(String taskCollectionsName) {
        this.taskCollectionsName = taskCollectionsName;
    }

    public int getTaskCollectionsColor() {
        return taskCollectionsColor;
    }

    public void setTaskCollectionsColor(int taskCollectionsColor) {
        this.taskCollectionsColor = taskCollectionsColor;
    }

    public List<TaskVo> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskVo> tasks) {
        this.tasks = tasks;
    }
}
