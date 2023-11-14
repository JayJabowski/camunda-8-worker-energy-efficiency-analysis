package jabowski.restworkerjava.events;

import org.springframework.stereotype.Component;

@Component
public class ConfigUpdateEvent {
    private Long pollInterval; //poll interval in milliseconds
    private Integer maxJobsActive; // maximum number of open jobs
    private Long timeout; // time to keep a poll open
    
    public Long getPollInterval() {
        return pollInterval;
    }
    public void setPollInterval(long pollInterval) {
        this.pollInterval = pollInterval;
    }
    public Integer getMaxJobsActive() {
        return maxJobsActive;
    }
    public void setMaxJobsActive(int maxJobsActive) {
        this.maxJobsActive = maxJobsActive;
    }
    public Long getTimeout() {
        return timeout;
    }
    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }
}
