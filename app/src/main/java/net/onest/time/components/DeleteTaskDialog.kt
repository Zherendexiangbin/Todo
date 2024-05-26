package net.onest.time.components

import android.app.Dialog
import android.content.Context
import android.widget.Toast
import com.lxj.xpopup.XPopup
import net.onest.time.api.TaskApi
import net.onest.time.api.vo.TaskVo
import net.onest.time.components.holder.AdapterHolder
import net.onest.time.utils.showToast

/**
 * 删除任务
 */
class DeleteTaskDialog(
    private val context: Context, // 要删除的taskVo
    private val taskVo: TaskVo, // 管理的列表
    private val tasks: List<TaskVo>, // 适配器
    private val adapter: AdapterHolder,
    private val dialog: Dialog
) {
    fun show() {
        XPopup.Builder(context)
            .asConfirm("",
                "你确定要删除这任项务吗？") {
                try {
                    TaskApi.removeTask(taskVo.taskId)
                    "删除成功！".showToast()
                } catch (e: RuntimeException) {
                    e.message?.showToast()
                }

                val position = tasks.indexOf(taskVo)
                (tasks as MutableList).removeAt(position)
                adapter.notifyItemRemoved(position)
                dialog.dismiss()
            }
            .show()
    }
}
