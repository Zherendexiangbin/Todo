package net.onest.time.api.vo.statistic;

/**
 * 某日番茄钟的统计
 */
public class DayTomatoStatistic {
    private Integer tomatoTimes;                // 番茄钟数（专注次数）
    private Long tomatoDuration;             // 番茄钟时长（专注时长）

    public DayTomatoStatistic() {
        this.tomatoTimes = 0;
        this.tomatoDuration = 0L;
    }

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

    @Override
    public String toString() {
        return "DayTomatoStatistic{" +
                "tomatoTimes=" + tomatoTimes +
                ", tomatoDuration=" + tomatoDuration +
                '}';
    }
}