package net.onest.time.api;

import net.onest.time.api.utils.RequestUtil;
import net.onest.time.api.vo.statistic.StatisticVo;

public class StatisticApi {
    private final static String PREFIX = "/statistic";

    // 获得统计数据
    private final static String STATISTIC = "/";

    // 每个任务的统计数据
    private final static String STATISTIC_BY_TASK = "/complex";

    // 每个任务的简化统计数据
    private final static String SIMPLE_STATISTIC_BY_TASK = "/simple";

    public static StatisticVo statistic() {
        return RequestUtil.builder()
                .url(ServerConstant.HTTP_ADDRESS + PREFIX + STATISTIC)
                .get()
                .buildAndSend(StatisticVo.class);
    }

    public static StatisticVo statisticByTask(Long taskId) {
        return RequestUtil.builder()
                .url(ServerConstant.HTTP_ADDRESS + PREFIX + STATISTIC_BY_TASK + "/" + taskId)
                .get()
                .buildAndSend(StatisticVo.class);
    }

    public static StatisticVo simpleStatisticByTask(Long taskId) {
        return RequestUtil.builder()
                .url(ServerConstant.HTTP_ADDRESS + PREFIX + SIMPLE_STATISTIC_BY_TASK + "/" + taskId)
                .get()
                .buildAndSend(StatisticVo.class);
    }
}
