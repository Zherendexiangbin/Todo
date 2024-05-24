package net.onest.time.api;

import net.onest.time.api.ServerConstant;
import net.onest.time.api.utils.RequestUtil;

public class RandomWordApi {
    private final static String PREFIX = "/word";

    // 获得随机一言
    private final static String GET_RANDOM_WORD = "/getRandomWord";

    public static String getRandomWord() {
        return RequestUtil.builder()
                .url(ServerConstant.HTTP_ADDRESS + PREFIX + GET_RANDOM_WORD)
                .get()
                .buildAndSend(String.class);
    }
}
