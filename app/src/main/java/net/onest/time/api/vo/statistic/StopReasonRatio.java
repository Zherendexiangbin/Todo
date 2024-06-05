package net.onest.time.api.vo.statistic;


public class StopReasonRatio {
    private String stopReason;   // 中断原因
    private Double ratio;

    public String getStopReason() {
        return stopReason;
    }

    public void setStopReason(String stopReason) {
        this.stopReason = stopReason;
    }

    public Double getRatio() {
        return ratio;
    }

    public void setRatio(Double ratio) {
        this.ratio = ratio;
    }

    @Override
    public String toString() {
        return "StopReasonRatio{" +
                "stopReason='" + stopReason + '\'' +
                ", ratio=" + ratio +
                '}';
    }
}

