package net.onest.time

import net.onest.time.utils.DateUtil
import net.onest.time.utils.dayString
import net.onest.time.utils.monthString
import net.onest.time.utils.toDate
import net.onest.time.utils.toLocalDate
import net.onest.time.utils.toLocalDateTime
import net.onest.time.utils.weekString
import org.junit.Assert
import org.junit.Test
import java.time.LocalDateTime

class DateTest {
    @Test
    fun dateTest() {
//        val epochMillisecond = DateUtil.epochMillisecond(2024, 5)
//
//        println(epochMillisecond)
//        Assert.assertEquals(epochMillisecond, 1714492800000)
        // 1714492800000

        println(LocalDateTime.now().monthString())
        println(LocalDateTime.now().weekString())
        println(LocalDateTime.now().dayString())

        println(LocalDateTime.now().minusMonths(3).minusDays(10).monthString())
        println(LocalDateTime.now().minusMonths(3).minusDays(10).weekString())
        println(LocalDateTime.now().minusMonths(3).minusDays(10).dayString())
    }

    @Test
    fun dateConvertTest() {
        val localDateTime = LocalDateTime.now()

        println(localDateTime.toDate())

        println(localDateTime.toDate().toLocalDateTime())
        println(localDateTime.toDate().toLocalDate())

        println(localDateTime.toDate().toLocalDate().toDate())
    }
}
