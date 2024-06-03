package net.onest.time.api;

import com.google.gson.reflect.TypeToken;

import net.onest.time.api.utils.RequestUtil;
import net.onest.time.api.utils.ResponseErrorException;
import net.onest.time.api.vo.UserVo;
import net.onest.time.api.vo.statistic.StatisticVo;

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


    public static StatisticVo statistic() {
        return RequestUtil.builder()
                .url(ServerConstant.HTTP_ADDRESS + PREFIX + STATISTIC)
                .get()
                .buildAndSend(StatisticVo.class);
    }

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
}
