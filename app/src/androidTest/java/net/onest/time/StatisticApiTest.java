package net.onest.time;

import net.onest.time.api.StatisticApi;

import org.junit.Test;

import java.util.Map;

public class StatisticApiTest {

    @Test
    public void statistic() {
        Map<String, Object> statistic = StatisticApi.statistic();
        System.out.println(statistic);
    }
}
