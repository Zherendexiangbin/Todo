package net.onest.time.api.vo;

import java.io.Serializable;
import java.util.Date; 
public class TomatoClock implements Serializable { 
        private Long clockId; 
        private Long taskId; 
        private Integer sequence; 
        private Integer clockStatus; 
        private String stopReason; 
        private Integer innerInterrupt; 
        private Integer outerInterrupt; 
        private Date startedAt; 
        private Date completedAt; 
        private Date createdAt;
        private Date updatedAt;
        private Integer deleted;

        public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", clockId=").append(clockId);
        sb.append(", taskId=").append(taskId);
        sb.append(", sequence=").append(sequence);
        sb.append(", clockStatus=").append(clockStatus);
        sb.append(", stopReason=").append(stopReason);
        sb.append(", innerInterrupt=").append(innerInterrupt);
        sb.append(", outerInterrupt=").append(outerInterrupt);
        sb.append(", startedAt=").append(startedAt);
        sb.append(", completedAt=").append(completedAt);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append(", deleted=").append(deleted);
        sb.append("]");
        return sb.toString();
    }
}