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
        StatisticVo statistic = StatisticApi.statistic();
        System.out.println(statistic);
    }

    @Test
    public void statisticByTask() {
        Long taskId = 122L;
        StatisticVo statistic = StatisticApi.statisticByTask(taskId);
        System.out.println(statistic);
    }
}
