package net.onest.time;

import net.onest.time.api.StatisticApi;
import net.onest.time.api.TaskApi;
import net.onest.time.api.vo.TaskVo;
import net.onest.time.api.vo.statistic.StatisticVo;

import org.junit.Test;

import java.util.List;

public class StatisticApiTest {

    @Test
    public void statistic() {
        StatisticVo statisticVo = StatisticApi.statistic();
        System.out.println(statisticVo);
    }

    @Test
    public void statisticByTask() {
        Long taskId = 122L;
        StatisticVo statisticVo = StatisticApi.statisticByTask(taskId);
        System.out.println(statisticVo);
    }

    @Test
    public void simpleStatisticByTask() {
        Long taskId = 227L;
        StatisticVo statisticVo = StatisticApi.simpleStatisticByTask(taskId);
        System.out.println(statisticVo);
    }
}
