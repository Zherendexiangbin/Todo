package net.onest.time;

import net.onest.time.api.StatisticApi;
import net.onest.time.api.TaskApi;
import net.onest.time.api.vo.TaskVo;
import net.onest.time.api.vo.statistic.DayTomatoStatistic;
import net.onest.time.api.vo.statistic.StatisticVo;
import net.onest.time.api.vo.statistic.StopReasonRatio;

import org.junit.Test;

import java.util.List;

public class StatisticApiTest {

    @Test
    public void statistic() {
        StatisticVo statisticVo = StatisticApi.statistic();
        System.out.println(statisticVo);
    }

    @Test
    public void statisticTime() {
        Long timestamp = 1716190853000L;
        StatisticVo statisticVo = StatisticApi.statistic(timestamp);
        System.out.println(statisticVo);
    }

    @Test
    public void statisticByTask() {
        Long taskId = 122L;
        Long timestamp = 1716190853000L;
        StatisticVo statisticVo = StatisticApi.statisticByTask(taskId, timestamp);
        System.out.println(statisticVo);
    }

    @Test
    public void simpleStatisticByTask() {
        Long taskId = 227L;
        StatisticVo statisticVo = StatisticApi.simpleStatisticByTask(taskId);
        System.out.println(statisticVo);
    }

    @Test
    public void statisticStopReason() {
        List<StopReasonRatio> stopReasonRatios = StatisticApi.statisticStopReason();
        System.out.println(stopReasonRatios);
    }

    @Test
    public void simpleStatisticToday() {
        DayTomatoStatistic dayTomatoStatistic = StatisticApi.simpleStatisticToday();
        System.out.println(dayTomatoStatistic);
    }
}
