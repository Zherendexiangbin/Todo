package net.onest.time.components;

import android.app.Dialog;
import android.content.Context;
import android.widget.BaseExpandableListAdapter;
import android.widget.Toast;

import com.lxj.xpopup.XPopup;

import net.onest.time.api.TaskApi;
import net.onest.time.api.vo.TaskVo;
import net.onest.time.components.holder.AdapterHolder;

import java.util.List;

/**
 * 删除任务
 */
public class DeleteTaskDialog {
    private final Context context;

    // 要删除的taskVo
    private final TaskVo taskVo;

    // 管理的列表
    private final List<TaskVo> tasks;

    // 适配器
    private final AdapterHolder adapter;
    private final Dialog dialog;

    public DeleteTaskDialog(Context context, TaskVo task, List<TaskVo> tasks, AdapterHolder adapter, Dialog dialog) {
        this.context = context;
        this.taskVo = task;
        this.tasks = tasks;
        this.adapter = adapter;
        this.dialog = dialog;
    }

    public void show() {
        new XPopup
                .Builder(context)
                .asConfirm("", "你确定要删除这项任务吗？",
                        () -> {
                            TaskApi.removeTask(taskVo.getTaskId());
                            Toast.makeText(context, "删除成功！", Toast.LENGTH_SHORT).show();
                            tasks.remove(taskVo);
                            adapter.notifyDataSetChanged();
                            dialog.dismiss();
                        })
                .show();
    }
}
