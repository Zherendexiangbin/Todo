package net.onest.time;

import net.onest.time.utils.DateUtil;

import org.junit.Test;

import java.util.Date;

public class DateUtilTest {
    @Test
    public void epochSecondTest() {
        Date date = new Date();
        Long epochSecond = DateUtil.epochMillisecond(date);
        System.out.println(epochSecond);
    }
}
