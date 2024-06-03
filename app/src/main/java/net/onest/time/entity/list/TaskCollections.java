package net.onest.time.entity.list;

import com.hgdendi.expandablerecycleradapter.BaseExpandableRecyclerViewAdapter;

import net.onest.time.api.vo.TaskVo;

import java.util.Comparator;
import java.util.List;

public class TaskCollections implements BaseExpandableRecyclerViewAdapter.BaseGroupBean<TaskVo> {
    private Long taskCollectionsId;
    private String taskCollectionsName;
    private int taskCollectionsColor;
    private List<TaskVo> tasks;

    public TaskCollections() {
    }

    public TaskCollections(Long taskCollectionsId, String taskCollectionsName, int taskCollectionsColor, List<TaskVo> tasks) {
        this.taskCollectionsId = taskCollectionsId;
        this.taskCollectionsName = taskCollectionsName;
        this.taskCollectionsColor = taskCollectionsColor;
        this.tasks = tasks;
    }

    public Long getTaskCollectionsId() {
        return taskCollectionsId;
    }

    public void setTaskCollectionsId(Long taskCollectionsId) {
        this.taskCollectionsId = taskCollectionsId;
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

    public static Comparator<TaskCollections> comparator() {
        return Comparator.comparing(TaskCollections::getTaskCollectionsId, Comparator.reverseOrder());
    }


    @Override
    public int getChildCount() {
        return tasks.size();
    }

    @Override
    public TaskVo getChildAt(int childIndex) {
        return tasks.size()<=childIndex?null:tasks.get(childIndex);
    }

    @Override
    public boolean isExpandable() {
        return getChildCount() > 0;
    }
}
