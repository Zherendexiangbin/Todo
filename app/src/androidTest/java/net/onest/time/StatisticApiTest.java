package net.onest.time;

import net.onest.time.api.StatisticApi;
import net.onest.time.api.TaskApi;
import net.onest.time.api.vo.TaskVo;
import net.onest.time.api.vo.statistic.StatisticVo;
import net.onest.time.utils.StatisticUtil;

import org.junit.Test;

import java.util.List;
import java.util.Map;

public class StatisticApiTest {

    @Test
    public void statistic() {
        StatisticVo statistic = StatisticApi.statistic();
        System.out.println(statistic);
    }

    @Test
    public void StatisticUtilTest() {
        List<TaskVo> list = TaskApi.findAll();
        list.forEach(taskVo ->
                System.out.println(StatisticUtil.from(taskVo))
        );
    }
}
