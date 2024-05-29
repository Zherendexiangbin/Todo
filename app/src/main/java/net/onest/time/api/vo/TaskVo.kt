package net.onest.time.api.vo

import java.io.Serializable
import java.util.Date
import java.util.Objects

class TaskVo : Serializable {
    var taskId: Long? = null
    var parentId: Long? = null
    var userId: Long? = null
    var taskName: String? = null
    var type: Int? = null

    // 0 清单页面
    // 1 清单集合
    // 2 已完成
    var taskStatus: Int? = null
    var clockDuration: Int? = null
    var remark: String? = null
    var estimate: MutableList<Int>? = null
    var restTime: Int? = null
    var again: Int? = null
    var categoryId: Long? = null
    var background: String? = null
    var startedAt: Date? = null
    var completedAt: Date? = null
    var createdAt: Date? = null


    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val taskVo = o as TaskVo
        return taskId == taskVo.taskId && parentId == taskVo.parentId && userId == taskVo.userId && taskName == taskVo.taskName && type == taskVo.type && taskStatus == taskVo.taskStatus && clockDuration == taskVo.clockDuration && remark == taskVo.remark && estimate == taskVo.estimate && restTime == taskVo.restTime && again == taskVo.again && categoryId == taskVo.categoryId && background == taskVo.background && startedAt == taskVo.startedAt && completedAt == taskVo.completedAt && createdAt == taskVo.createdAt
    }

    override fun hashCode(): Int {
        return Objects.hash(
            taskId,
            parentId,
            userId,
            taskName,
            type,
            taskStatus,
            clockDuration,
            remark,
            estimate,
            restTime,
            again,
            categoryId,
            background,
            startedAt,
            completedAt,
            createdAt
        )
    }

    override fun toString(): String {
        return "TaskVo{" +
                "taskId=" + taskId +
                ", parentId=" + parentId +
                ", userId=" + userId +
                ", taskName='" + taskName + '\'' +
                ", type=" + type +
                ", taskStatus=" + taskStatus +
                ", clockDuration=" + clockDuration +
                ", remark='" + remark + '\'' +
                ", estimate=" + estimate +
                ", restTime=" + restTime +
                ", again=" + again +
                ", categoryId=" + categoryId +
                ", background='" + background + '\'' +
                ", startedAt=" + startedAt +
                ", completedAt=" + completedAt +
                ", createdAt=" + createdAt +
                '}'
    }

    companion object {
        fun comparator(): Comparator<TaskVo> =
            Comparator.comparing(TaskVo::taskStatus) { a, b ->
                // 未完成的排前面
                if (a != 2 && b == 2) {
                    -1
                } else if (a == 2 && b != 2) {
                    1
                } else {
                    0
                }
            }.thenComparing(TaskVo::createdAt) { a, b ->
                // 按时间排序
                if (a!!.before(b)) {
                    1
                } else if (a.after(b)) {
                    -1
                } else {
                    0
                }
            }

    }
}
