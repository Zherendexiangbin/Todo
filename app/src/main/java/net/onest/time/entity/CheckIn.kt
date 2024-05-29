package net.onest.time.entity

import net.onest.time.api.vo.statistic.TaskRatio


data class CheckIn(val username: String, val tomatoTimes: Int, val tomatoDuration: Double, val taskRatio: List<TaskRatio>)