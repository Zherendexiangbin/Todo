package net.onest.time;

import net.onest.time.api.TomatoClockApi;
import net.onest.time.api.vo.TomatoClockVo;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class TomatoApiTest {
    @Test
    public void addTomatoClock() {
        List<TomatoClockVo> tomatoClockVos = TomatoClockApi.addTomatoClock(122L);
        Assert.assertNotNull(tomatoClockVos);
    }

    @Test
    public void completeTomatoClock() {
        Long clockId = 1235L;
        TomatoClockApi.completeTomatoClock(clockId);
    }

    @Test
    public void innerInterrupt() {
        Long clockId = 59L;
        TomatoClockApi.innerInterrupt(clockId);
    }

    @Test
    public void outerInterrupt() {
        Long clockId = 59L;
        TomatoClockApi.outerInterrupt(clockId);
    }

    @Test
    public void startTomatoClock() {
        Long clockId = 1235L;
        TomatoClockApi.startTomatoClock(clockId);
    }

    @Test
    public void stopTomatoClock() {
        Long taskId = 28L;
        TomatoClockApi.stopTomatoClock(taskId, "爷不想玩啦，你管我？");
    }

    @Test
    public void findTomatoClock() {
        Long clockId = 60L;
        TomatoClockVo tomatoClock = TomatoClockApi.findTomatoClock(clockId);
        Assert.assertNotNull(tomatoClock);
    }

    @Test
    public void findTomatoClockAll() {
        Long taskId = 28L;
        List<TomatoClockVo> tomatoClockVoList = TomatoClockApi.findTomatoClockAll(taskId);
        Assert.assertNotNull(tomatoClockVoList);
    }

    @Test
    public void deleteTomatoClock() {
        Long taskId = 28L;
        TomatoClockApi.deleteTomatoClock(taskId);
    }
}
