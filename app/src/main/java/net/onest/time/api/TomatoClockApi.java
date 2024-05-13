package net.onest.time.api;

import com.google.gson.reflect.TypeToken;

import net.onest.time.api.utils.RequestUtil;
import net.onest.time.api.vo.TomatoClockVo;

import java.util.List;

public class TomatoClockApi {
    private final static String PREFIX = "/clock";

    // 添加番茄钟
    private final static String ADD_TOMATO_CLOCK = "/addTomatoClock";

    // 开始执行一个番茄钟
    private final static String START_TOMATO_CLOCK = "/startTomatoClock";

    // 完成一个番茄钟
    private final static String COMPLETE_TOMATO_CLOCK = "/completeTomatoClock";

    // 内部中断
    private final static String INNER_INTERRUPT = "/innerInterrupt";

    // 外部中断
    private final static String OUTER_INTERRUPT = "/outerInterrupt";

    // 停止番茄钟
    private final static String STOP_TOMATO_CLOCK = "/stopTomatoClock";

    // 查询一个番茄钟
    private final static String FIND_TOMATO_CLOCK = "/findTomatoClock";

    // 查询一个任务的所有番茄钟
    private final static String FIND_TOMATO_CLOCK_ALL = "/findTomatoClockAll";

    // 删除番茄钟
    private final static String DELETE_TOMATO_CLOCK = "/deleteTomatoClock";

    // 添加番茄钟  --------点击 开始按钮 发送该请求
    public static List<TomatoClockVo> addTomatoClock(Long taskId, Integer estimate){
        return RequestUtil.builder()
                .url(ServerConstant.ADDRESS + PREFIX + ADD_TOMATO_CLOCK + "/" + taskId + "/" + estimate)
                .post()
                .buildAndSend(new TypeToken<List<TomatoClockVo>>(){});
    }

    // 开始执行一个番茄钟
    // ----------  第一个番茄钟 不需要发送该请求
    // ----------  后续番茄钟 都需要发送该请求
    public static void startTomatoClock(Long clockId) {
        RequestUtil.builder()
            .url(ServerConstant.ADDRESS + PREFIX + START_TOMATO_CLOCK + "/" + clockId)
            .put()
            .buildAndSend();
    }

    // 完成一个番茄钟  ---------- 完成任一 番茄钟 都需要发送该请求
    public static void completeTomatoClock(Long clockId) {
        RequestUtil.builder()
                .url(ServerConstant.ADDRESS + PREFIX + COMPLETE_TOMATO_CLOCK + "/" + clockId)
                .put()
                .buildAndSend();
    }

    // 内部中断 ------------ 内部中断 点击事件
    public static void innerInterrupt(Long clockId) {
        RequestUtil.builder()
                .url(ServerConstant.ADDRESS + PREFIX + INNER_INTERRUPT + "/" + clockId)
                .put()
                .buildAndSend();
    }

    // 外部中断 ------------ 外部中断 点击事件
    public static void outerInterrupt(Long clockId) {
        RequestUtil.builder()
                .url(ServerConstant.ADDRESS + PREFIX + OUTER_INTERRUPT + "/" + clockId)
                .put()
                .buildAndSend();
    }

    // 停止番茄钟 ------------- 任务提前终止  参数 stopReason停止原因 为可选参数  不强制传参
    public static void stopTomatoClock(Long taskId, String stopReason){
        RequestUtil.builder()
                .url(ServerConstant.ADDRESS + PREFIX + STOP_TOMATO_CLOCK + "/" + taskId + "?stopReason=" + stopReason)
                .put()
                .buildAndSend();
    }

    // 查询一个番茄钟
    public static TomatoClockVo findTomatoClock(Long clockId) {
        return RequestUtil.builder()
                    .url(ServerConstant.ADDRESS + PREFIX + FIND_TOMATO_CLOCK + "/" + clockId)
                    .get()
                    .buildAndSend(TomatoClockVo.class);
    }

    // 查询一个任务的所有番茄钟 --------  点击 任务 查询详情
    public static List<TomatoClockVo> findTomatoClockAll(Long taskId) {
        return RequestUtil.builder()
                .url(ServerConstant.ADDRESS + PREFIX + FIND_TOMATO_CLOCK_ALL + "/" + taskId)
                .get()
                .buildAndSend(new TypeToken<List<TomatoClockVo>>(){});
    }

    // 删除番茄钟  ------------- 任务已完成  但 用户把该任务删除
    public static void deleteTomatoClock(Long taskId) {
        RequestUtil.builder()
                .url(ServerConstant.ADDRESS + PREFIX + DELETE_TOMATO_CLOCK + "/" + taskId)
                .delete()
                .buildAndSend();
    }
}
