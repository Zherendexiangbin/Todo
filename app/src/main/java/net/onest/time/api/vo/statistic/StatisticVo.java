package net.onest.time.api.vo.statistic;

import java.util.List;
import java.util.Map;

/**
 * 数据统计类
 */
public class StatisticVo {
    private Integer tomatoTimes;                                // 总番茄钟数（总专注次数）
    private Long tomatoDuration;                                // 总番茄钟时长（总专注时长）
    private Map<Long, DayTomatoStatistic> dayTomatoMap;         // 某日番茄钟统计
    private Long avgTomatoDuration;                          // 每天平均专注时长
    private Integer avgTomatoTimes;                             // 每天平均专注次数
    private Boolean cached;                                     // 是否是Redis缓存
    private Integer tomatoDays;                                 // 一共使用的天数
    private List<TaskRatio> ratioByDurationOfDay;               // 通过专注时长 按天
    private List<TaskRatio> ratioByDurationOfWeek;              // 通过专注时长 按周
    private List<TaskRatio> ratioByDurationOfMonth;             // 通过专注时长 按月

    public Integer getTomatoTimes() {
        return tomatoTimes;
    }

    public void setTomatoTimes(Integer tomatoTimes) {
        this.tomatoTimes = tomatoTimes;
    }

    public Long getTomatoDuration() {
        return tomatoDuration;
    }

    public void setTomatoDuration(Long tomatoDuration) {
        this.tomatoDuration = tomatoDuration;
    }

    public Map<Long, DayTomatoStatistic> getDayTomatoMap() {
        return dayTomatoMap;
    }

    public void setDayTomatoMap(Map<Long, DayTomatoStatistic> dayTomatoMap) {
        this.dayTomatoMap = dayTomatoMap;
    }

    public Long getAvgTomatoDuration() {
        return avgTomatoDuration;
    }

    public void setAvgTomatoDuration(Long avgTomatoDuration) {
        this.avgTomatoDuration = avgTomatoDuration;
    }

    public Integer getAvgTomatoTimes() {
        return avgTomatoTimes;
    }

    public void setAvgTomatoTimes(Integer avgTomatoTimes) {
        this.avgTomatoTimes = avgTomatoTimes;
    }

    public Boolean getCached() {
        return cached;
    }

    public void setCached(Boolean cached) {
        this.cached = cached;
    }

    public Integer getTomatoDays() {
        return tomatoDays;
    }

    public void setTomatoDays(Integer tomatoDays) {
        this.tomatoDays = tomatoDays;
    }

    public List<TaskRatio> getRatioByDurationOfDay() {
        return ratioByDurationOfDay;
    }

    public void setRatioByDurationOfDay(List<TaskRatio> ratioByDurationOfDay) {
        this.ratioByDurationOfDay = ratioByDurationOfDay;
    }

    public List<TaskRatio> getRatioByDurationOfWeek() {
        return ratioByDurationOfWeek;
    }

    public void setRatioByDurationOfWeek(List<TaskRatio> ratioByDurationOfWeek) {
        this.ratioByDurationOfWeek = ratioByDurationOfWeek;
    }

    public List<TaskRatio> getRatioByDurationOfMonth() {
        return ratioByDurationOfMonth;
    }

    public void setRatioByDurationOfMonth(List<TaskRatio> ratioByDurationOfMonth) {
        this.ratioByDurationOfMonth = ratioByDurationOfMonth;
    }

    @Override
    public String toString() {
        return "StatisticVo{" +
                "tomatoTimes=" + tomatoTimes +
                ", tomatoDuration=" + tomatoDuration +
                ", dayTomatoMap=" + dayTomatoMap +
                ", avgTomatoDuration=" + avgTomatoDuration +
                ", avgTomatoTimes=" + avgTomatoTimes +
                ", cached=" + cached +
                ", tomatoDays=" + tomatoDays +
                ", ratioByDurationOfDay=" + ratioByDurationOfDay +
                ", ratioByDurationOfWeek=" + ratioByDurationOfWeek +
                ", ratioByDurationOfMonth=" + ratioByDurationOfMonth +
                '}';
    }
}
