package jabowski.restworkerjava.config;

import org.springframework.stereotype.Component;

import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
public class localWorkerConfig {
    
    private long timeout; // time to keep a poll open
    private long pollInterval; //poll interval in milliseconds
    private int maxJobsActive; // maximum number of open jobs
    private String JobType;

    public String getJobType() {
        return JobType;
    }
    public localWorkerConfig setJobType(String jobType) {
        JobType = jobType;
        return this;
    }
    public long getPollInterval() {
        return pollInterval;
    }
    public localWorkerConfig setPollInterval(long pollInterval) {
        this.pollInterval = pollInterval;
        return this;
    }
    public int getMaxJobsActive() {
        return maxJobsActive;
    }
    public localWorkerConfig setMaxJobsActive(int maxJobsActive) {
        this.maxJobsActive = maxJobsActive;
        return this;
    }
    public long getTimeout() {
        return timeout;
    }
    public localWorkerConfig setTimeout(long timeout) {
        this.timeout = timeout;
        return this;
    }
    
}
