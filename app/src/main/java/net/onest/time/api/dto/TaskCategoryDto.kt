package net.onest.time.api.dto

import net.onest.time.api.vo.TaskCategoryVo
import net.onest.time.api.vo.TaskVo

class TaskCategoryDto {
    var categoryId: Long? = null
    var categoryName: String? = null
    var color: Int? = null

    fun withDefault(): TaskCategoryDto {
        categoryId=null
        categoryName=""
        color=null
//        taskId = null
//        userId = null
//        taskName = ""
//        type = 0
//        clockDuration = 1
//        remark = ""
//        estimate = ArrayList()
//        restTime = 5
//        again = 0
//        categoryId = null
//        tomatoClockTimes = 1
//        tomatoClocks = ArrayList()
//        stopTimes = 0
//        taskStatus = 0
//        innerInterrupt = 0
//        outerInterrupt = 0
//        startedAt = null
//        completedAt = null
        return this
    }
    fun withTaskCategoryVo(taskCategoryVo: TaskCategoryVo): TaskCategoryDto {
        categoryId=taskCategoryVo.categoryId
        categoryName=taskCategoryVo.categoryName
        color=taskCategoryVo.color
        return this
    }
}