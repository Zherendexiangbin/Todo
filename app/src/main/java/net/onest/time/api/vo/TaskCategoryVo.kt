package net.onest.time.api.vo

class TaskCategoryVo {
    var categoryId: Long? = null
    var categoryName: String? = null
    var color: Int? = null
    var taskVos: List<TaskVo>? = null
    override fun toString(): String {
        return "TaskCategoryVo(categoryId=$categoryId, categoryName=$categoryName, color=$color, taskVos=$taskVos)"
    }


}