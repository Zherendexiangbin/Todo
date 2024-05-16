package net.onest.time.api;

import net.onest.time.api.utils.RequestUtil;
import net.onest.time.api.vo.statistic.StatisticVo;

public class StatisticApi {
    private final static String PREFIX = "/statistic";

    // 获得统计数据
    private final static String STATISTIC = "/";

    public static StatisticVo statistic() {
        return RequestUtil.builder()
                .url(ServerConstant.HTTP_ADDRESS + PREFIX + STATISTIC)
                .get()
                .buildAndSend(StatisticVo.class);
    }
}
