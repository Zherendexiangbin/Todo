package net.onest.time.api.vo

import com.hgdendi.expandablerecycleradapter.BaseExpandableRecyclerViewAdapter

class TaskCategoryVo(
    var categoryId: Long?,
    var categoryName: String,
    var color: Int,
    var taskVos: List<TaskVo>
): BaseExpandableRecyclerViewAdapter.BaseGroupBean<TaskVo?> {

    override fun toString(): String {
        return "TaskCategoryVo(categoryId=$categoryId, categoryName=$categoryName, color=$color, taskVos=$taskVos)"
    }

    override fun getChildCount() = taskVos.size

    override fun getChildAt(childIndex: Int) = taskVos[childIndex]

    override fun isExpandable() = childCount > 0


}