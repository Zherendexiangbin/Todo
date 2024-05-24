package net.onest.time.api.dto

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

    var category: String? = null

    var tomatoClockTimes: Int? = null
    var tomatoClocks: List<TomatoClock>? = null

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
        type = 1
        clockDuration = 1
        remark = ""
        estimate = ArrayList()
        restTime = 5
        again = 0
        category = ""
        tomatoClockTimes = 1
        tomatoClocks = ArrayList()
        stopTimes = 0
        taskStatus = 0
        innerInterrupt = 0
        outerInterrupt = 0
        startedAt = Date()
        completedAt = Date()
        return this;
    }
}
