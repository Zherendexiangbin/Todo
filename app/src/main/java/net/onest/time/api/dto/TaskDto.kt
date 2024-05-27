package net.onest.time.api.dto

import net.onest.time.api.vo.TaskVo
import net.onest.time.api.vo.TomatoClock
import java.util.Date

class TaskDto {
    var taskId: Long? = null
    var userId: Long? = null

    var taskName: String? = null
    var type: Int? = null
    var clockDuration: Int? = null

    var remark: String? = null

    var estimate: ArrayList<Int>? = null

    var restTime: Int? = null

    var again: Int? = null

    var categoryId: Long? = null

    var tomatoClockTimes: Int? = null
    var tomatoClocks: ArrayList<TomatoClock>? = null

    var stopTimes: Int? = null

    var taskStatus: Int? = null

    var innerInterrupt: Int? = null
    var outerInterrupt: Int? = null

    var startedAt: Date? = null
    var completedAt: Date? = null

    fun withDefault(): TaskDto {
        taskId = null
        userId = null
        taskName = ""
        type = 0
        clockDuration = 1
        remark = ""
        estimate = ArrayList()
        restTime = 5
        again = 0
        categoryId = null
        tomatoClockTimes = 1
        tomatoClocks = ArrayList()
        stopTimes = 0
        taskStatus = 0
        innerInterrupt = 0
        outerInterrupt = 0
        startedAt = null
        completedAt = null
        return this
    }

    fun withTaskVo(task: TaskVo): TaskDto {
        taskId = task.taskId
        userId = task.userId
        taskName = task.taskName
        type = task.type
        clockDuration = task.clockDuration
        remark = task.remark
        estimate = ArrayList(task.estimate)
        restTime = task.restTime
        again = task.again
        categoryId = task.categoryId
        tomatoClockTimes = task.tomatoClockTimes

        val clocks = task.tomatoClocks?.run {
            map { TomatoClock().withTomatoClockVo(it) }
        }
        tomatoClocks = if (clocks != null) ArrayList(clocks) else ArrayList()

        stopTimes = task.stopTimes
        taskStatus = task.taskStatus
        innerInterrupt = task.innerInterrupt
        outerInterrupt = task.outerInterrupt
        startedAt = task.startedAt
        completedAt = task.completedAt

        return this
    }
}
