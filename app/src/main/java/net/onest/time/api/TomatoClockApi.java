package net.onest.time.api;

import com.google.gson.reflect.TypeToken;

import net.onest.time.api.utils.RequestUtil;
import net.onest.time.api.vo.TomatoClockVo;

import java.util.List;
import java.util.Map;

public class TomatoClockApi {
    private final static String PREFIX = "/clock";

    // 添加番茄钟
    private final static String ADD_TOMATO_CLOCK = "/addTomatoClock";

    // 开始执行一个番茄钟
    private final static String START_TOMATO_CLOCK = "/startTomatoClock";

    // 完成一个番茄钟
    private final static String COMPLETE_TOMATO_CLOCK = "/completeTomatoClock";

    // 停止番茄钟
    private final static String STOP_TOMATO_CLOCK = "/stopTomatoClock";

    // 提前完成任务
    private final static String ADVANCE_COMPLETE_TASK = "/advanceCompleteTask";

    // 查询一个番茄钟
    private final static String FIND_TOMATO_CLOCK = "/findTomatoClock";

    // 查询一个任务的所有番茄钟
    private final static String FIND_TOMATO_CLOCK_ALL = "/findTomatoClockAll";

    // 删除番茄钟
    private final static String DELETE_TOMATO_CLOCK = "/deleteTomatoClock";

    // 每个任务的专注历史记录
    private final static String STATISTIC_HISTORY_BY_TASK = "/";

    // 每个任务的专注历史记录
    private final static String STATISTIC_HISTORY_BY_USER = "/user";

    // 添加番茄钟  --------点击 开始按钮 发送该请求
    public static List<TomatoClockVo> addTomatoClock(Long taskId){
        return RequestUtil.builder()
                .url(ServerConstant.HTTP_ADDRESS + PREFIX + ADD_TOMATO_CLOCK + "/" + taskId)
                .post()
                .buildAndSend(new TypeToken<List<TomatoClockVo>>(){});
    }

    // 开始执行一个番茄钟
    // ----------  第一个番茄钟 不需要发送该请求
    // ----------  后续番茄钟 都需要发送该请求
    public static void startTomatoClock(Long clockId) {
        RequestUtil.builder()
            .url(ServerConstant.HTTP_ADDRESS + PREFIX + START_TOMATO_CLOCK + "/" + clockId)
            .put()
            .buildAndSend();
    }

    // 完成一个番茄钟  ---------- 完成任一 番茄钟 都需要发送该请求
    public static void completeTomatoClock(Long clockId) {
        RequestUtil.builder()
                .url(ServerConstant.HTTP_ADDRESS + PREFIX + COMPLETE_TOMATO_CLOCK + "/" + clockId)
                .put()
                .buildAndSend();
    }

    // 停止番茄钟 ------------- 任务提前终止  参数 stopReason停止原因 为可选参数  不强制传参
    public static void stopTomatoClock(Long taskId, String stopReason){
        RequestUtil.builder()
                .url(ServerConstant.HTTP_ADDRESS + PREFIX + STOP_TOMATO_CLOCK + "/" + taskId + "?stopReason=" + stopReason)
                .put()
                .buildAndSend();
    }

    // 查询一个番茄钟
    public static TomatoClockVo findTomatoClock(Long clockId) {
        return RequestUtil.builder()
                    .url(ServerConstant.HTTP_ADDRESS + PREFIX + FIND_TOMATO_CLOCK + "/" + clockId)
                    .get()
                    .buildAndSend(TomatoClockVo.class);
    }

    // 查询一个任务的所有番茄钟 --------  点击 任务 查询详情
    public static List<TomatoClockVo> findTomatoClockAll(Long taskId) {
        return RequestUtil.builder()
                .url(ServerConstant.HTTP_ADDRESS + PREFIX + FIND_TOMATO_CLOCK_ALL + "/" + taskId)
                .get()
                .buildAndSend(new TypeToken<List<TomatoClockVo>>(){});
    }

    // 删除番茄钟  ------------- 任务已完成  但 用户把该任务删除
    public static void deleteTomatoClock(Long taskId) {
        RequestUtil.builder()
                .url(ServerConstant.HTTP_ADDRESS + PREFIX + DELETE_TOMATO_CLOCK + "/" + taskId)
                .delete()
                .buildAndSend();
    }

    // 每个任务的专注历史记录
    public static Map<Long, List<TomatoClockVo>> statisticHistoryByTask(Long taskId) {
        return RequestUtil.builder()
                    .url(ServerConstant.HTTP_ADDRESS + PREFIX + STATISTIC_HISTORY_BY_TASK + taskId)
                    .get()
                    .buildAndSend(new TypeToken<Map<Long, List<TomatoClockVo>>>(){});
    }

    // 每位用户的专注历史记录
    public static Map<Long, List<TomatoClockVo>> statisticHistoryByUser() {
        return RequestUtil.builder()
                .url(ServerConstant.HTTP_ADDRESS + PREFIX + STATISTIC_HISTORY_BY_USER)
                .get()
                .buildAndSend(new TypeToken<Map<Long, List<TomatoClockVo>>>(){});
    }

    public static void advanceCompleteTask(Long taskId) {
        RequestUtil.builder()
            .url(ServerConstant.HTTP_ADDRESS + PREFIX + ADVANCE_COMPLETE_TASK + "/" + taskId)
            .put()
            .buildAndSend();
    }
}
