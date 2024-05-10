package net.onest.time.api.vo;

import java.util.List;

public class Page<T> {
    private static final long serialVersionUID = 8545996863226528798L;
    protected List<T> records;
    protected long total;
    protected long size;
    protected long current;
    protected boolean optimizeCountSql;
    protected boolean searchCount;
    protected boolean optimizeJoinOfCountSql;
    protected Long maxLimit;
    protected String countId;

    public Page() {
    }

    public Page(List<T> records, long total, long size, long current, boolean optimizeCountSql, boolean searchCount, boolean optimizeJoinOfCountSql, Long maxLimit, String countId) {
        this.records = records;
        this.total = total;
        this.size = size;
        this.current = current;
        this.optimizeCountSql = optimizeCountSql;
        this.searchCount = searchCount;
        this.optimizeJoinOfCountSql = optimizeJoinOfCountSql;
        this.maxLimit = maxLimit;
        this.countId = countId;
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getCurrent() {
        return current;
    }

    public void setCurrent(long current) {
        this.current = current;
    }

    public boolean isOptimizeCountSql() {
        return optimizeCountSql;
    }

    public void setOptimizeCountSql(boolean optimizeCountSql) {
        this.optimizeCountSql = optimizeCountSql;
    }

    public boolean isSearchCount() {
        return searchCount;
    }

    public void setSearchCount(boolean searchCount) {
        this.searchCount = searchCount;
    }

    public boolean isOptimizeJoinOfCountSql() {
        return optimizeJoinOfCountSql;
    }

    public void setOptimizeJoinOfCountSql(boolean optimizeJoinOfCountSql) {
        this.optimizeJoinOfCountSql = optimizeJoinOfCountSql;
    }

    public Long getMaxLimit() {
        return maxLimit;
    }

    public void setMaxLimit(Long maxLimit) {
        this.maxLimit = maxLimit;
    }

    public String getCountId() {
        return countId;
    }

    public void setCountId(String countId) {
        this.countId = countId;
    }

    @Override
    public String toString() {
        return "Page{" +
                "records=" + records +
                ", total=" + total +
                ", size=" + size +
                ", current=" + current +
                ", optimizeCountSql=" + optimizeCountSql +
                ", searchCount=" + searchCount +
                ", optimizeJoinOfCountSql=" + optimizeJoinOfCountSql +
                ", maxLimit=" + maxLimit +
                ", countId='" + countId + '\'' +
                '}';
    }
}

