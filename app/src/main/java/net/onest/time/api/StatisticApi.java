package net.onest.time.api;

import com.google.gson.reflect.TypeToken;

import net.onest.time.api.utils.RequestUtil;
import net.onest.time.api.utils.ResponseErrorException;
import net.onest.time.api.vo.UserVo;
import net.onest.time.api.vo.statistic.DayTomatoStatistic;
import net.onest.time.api.vo.statistic.StatisticVo;
import net.onest.time.api.vo.statistic.StopReasonRatio;

import java.util.List;
import java.util.function.Consumer;

public class StatisticApi {
    private final static String PREFIX = "/statistic";

    // 获得统计数据
    private final static String STATISTIC = "/";

    // 每个任务的统计数据
    private final static String STATISTIC_BY_TASK = "/complex";

    // 每个任务的简化统计数据
    private final static String SIMPLE_STATISTIC_BY_TASK = "/simple";

    // 排行榜
    private final static String RANKLING_LIST = "/rankingList";

    // 统计中断原因
    private final static String STATISTIC_STOP_REASON = "/stopReason";

    // 获得简化版今日统计数据
    private final static String SIMPLE_STATISTIC_TODAY = "/simpleStatisticToday";

    // 每个清单集合的统计数据
    private final static String STATISTIC_BY_CATEGORY = "/category";

    public static StatisticVo statistic(Long timestamp) {
        return RequestUtil.builder()
                .url(ServerConstant.HTTP_ADDRESS + PREFIX + STATISTIC + timestamp)
                .get()
                .buildAndSend(StatisticVo.class);
    }

    public static StatisticVo statisticByTask(Long taskId, Long timestamp) {
        return RequestUtil.builder()
                .url(ServerConstant.HTTP_ADDRESS + PREFIX + STATISTIC_BY_TASK + "/" + taskId + "/" + timestamp)
                .get()
                .buildAndSend(StatisticVo.class);
    }

    public static StatisticVo simpleStatisticByTask(Long taskId) {
        return RequestUtil.builder()
                .url(ServerConstant.HTTP_ADDRESS + PREFIX + SIMPLE_STATISTIC_BY_TASK + "/" + taskId)
                .get()
                .buildAndSend(StatisticVo.class);
    }

    public static void rankingList(
            Consumer<List<UserVo>> consumer,
            Consumer<ResponseErrorException> exceptionHandler
    ) {
        RequestUtil.builder()
            .url(ServerConstant.HTTP_ADDRESS + PREFIX + RANKLING_LIST)
            .get()
            .submit(new TypeToken<List<UserVo>>(){}, consumer, exceptionHandler);
    }

    public static List<StopReasonRatio> statisticStopReason() {
        return RequestUtil.builder()
                .url(ServerConstant.HTTP_ADDRESS + PREFIX + STATISTIC_STOP_REASON)
                .get()
                .buildAndSend(new TypeToken<List<StopReasonRatio>>(){});
    }

    public static DayTomatoStatistic simpleStatisticToday() {
        return RequestUtil.builder()
                .url(ServerConstant.HTTP_ADDRESS + PREFIX + SIMPLE_STATISTIC_TODAY)
                .get()
                .buildAndSend(new TypeToken<DayTomatoStatistic>(){});
    }

    public static StatisticVo statisticByCategory(Long categoryId, Long timestamp) {
        return RequestUtil.builder()
                .url(ServerConstant.HTTP_ADDRESS + PREFIX + STATISTIC_BY_CATEGORY + "/" + categoryId + "/" + timestamp)
                .get()
                .buildAndSend(new TypeToken<StatisticVo>(){});
    }
}
