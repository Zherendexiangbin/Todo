package net.onest.time.utils;

import net.onest.time.api.vo.TaskVo;
import net.onest.time.api.vo.TomatoClock;
import net.onest.time.api.vo.statistic.DayTomatoStatistic;
import net.onest.time.api.vo.statistic.StatisticVo;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * 统计数据的工具类
 */
public class StatisticUtil {
    public static StatisticVo from(TaskVo taskVo) {
        Integer tomatoDays;
        if (taskVo.getTaskStatus() == 2) {
            // 已完成
            tomatoDays = (int) ((taskVo.getCompletedAt().getTime() - taskVo.getStartedAt().getTime()) / 60 / 60 / 24);
        } else {
            tomatoDays = (int) ((new Date().getTime() - taskVo.getStartedAt().getTime()) / 60 / 60 / 24);
        }

        AtomicInteger tomatoTimes = new AtomicInteger(0);
        AtomicLong tomatoDuration = new AtomicLong(0);
        HashMap<Long, List<TomatoClock>> dayTomatoTmp = taskVo.getTomatoClocks().stream()
                .filter(tomatoClock -> tomatoClock.getClockStatus() == TomatoClock.Status.COMPLETE)
                .peek(tomatoClock -> {
                    tomatoTimes.addAndGet(1);
                    tomatoDuration.addAndGet(taskVo.getClockDuration());
                })
                .collect(Collectors.groupingBy(
                        // 获取某一天的时间戳，并通过这个分组
                        tomatoClock -> DateUtil.epochMillisecond(tomatoClock.getCompletedAt()),
                        HashMap::new,
                        Collectors.toList())
                );

        // 每天的总专注
        Map<Long, DayTomatoStatistic> dayTomatoMap = new HashMap<>();
        dayTomatoTmp.forEach((k, v) -> {
            DayTomatoStatistic dayTomato = v.stream()
                    .map(t -> {
                        DayTomatoStatistic dayTomatoStatistic = new DayTomatoStatistic();
                        dayTomatoStatistic.setTomatoTimes(1);
                        dayTomatoStatistic.setTomatoDuration(t.getCompletedAt().getTime() - t.getStartedAt().getTime());
                        return dayTomatoStatistic;
                    })
                    .reduce(new DayTomatoStatistic(), (s1, s2) -> {
                        s1.setTomatoTimes(s1.getTomatoTimes() + 1);
                        s1.setTomatoDuration(s1.getTomatoDuration() + s2.getTomatoDuration());
                        return s1;
                    });
            dayTomatoMap.put(k, dayTomato);
        });

        StatisticVo statisticVo = new StatisticVo();
        statisticVo.setCached(false);
        statisticVo.setTomatoTimes(tomatoTimes.get());
        statisticVo.setTomatoDuration(tomatoDuration.get());
        statisticVo.setDayTomatoMap(dayTomatoMap);

//        // 专注时长分布
//        statisticVo.setRatioByDurationOfDay(getFocusDuration(dayTomatoTmp, DAY));
//        statisticVo.setRatioByDurationOfWeek(getFocusDuration(dayTomatoTmp, WEEK));
//        statisticVo.setRatioByDurationOfMonth(getFocusDuration(dayTomatoTmp, MONTH));

        // 日均时长
        statisticVo.setAvgTomatoTimes(tomatoDays == 0 ? 0 : (int) (tomatoDuration.get() / tomatoDays));
        statisticVo.setAvgTomatoDuration(tomatoDays == 0 ? 0 : tomatoTimes.get() / tomatoDays);

        return statisticVo;
    }

//    private static List<TaskRatio> getFocusDuration(HashMap<Long, List<TomatoClock>> dayTomatoTmp, DateUtil.Unit timeUnit) {
//        // exclusive
//        long start = timeUnit.getStartEpochSecond();
//
//        // inclusive
//        long end = timeUnit.getEndEpochSecond();
//
//        Map<String, Integer> tmp = new HashMap<>();
//        AtomicInteger sum = new AtomicInteger();
//        dayTomatoTmp.forEach((k, v) -> {
//            if (k >= start && k <= end) {
//                v.forEach(t -> {
//                    int duration = t.getClockDuration() * t.getTomatoClockTimes();
//                    tmp.merge(t.getTaskName(), duration, Integer::sum);
//                    sum.addAndGet(duration);
//                });
//            }
//        });
//
//        HashMap<String, Double> res = new HashMap<>();
//        tmp.forEach((k, v) -> res.put(k, 1.0 * v / sum.get()));
//        return res;
//    }
}
