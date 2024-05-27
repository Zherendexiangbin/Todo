package net.onest.time

import net.onest.time.utils.DateUtil
import org.junit.Assert
import org.junit.Test

class DateTest {
    @Test
    fun dateTest() {
        val epochMillisecond = DateUtil.epochMillisecond(2024, 5)

        println(epochMillisecond)
        Assert.assertEquals(epochMillisecond, 1714492800000)
        // 1714492800000
    }
}
