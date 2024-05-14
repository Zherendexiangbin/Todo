package net.onest.time.api;

import com.google.gson.reflect.TypeToken;

import net.onest.time.api.utils.RequestUtil;

import java.util.Map;

public class StatisticApi {
    private final static String PREFIX = "/statistic";

    // 获得统计数据
    private final static String STATISTIC = "/";

    public static Map<String, Object> statistic() {
        return RequestUtil.builder()
                .url(ServerConstant.HTTP_ADDRESS + PREFIX + STATISTIC)
                .get()
                .buildAndSend(new TypeToken<Map<String, Object>>(){});
    }
}
