package jabowski.restworkerjava.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="worker-config")
public class WorkerConfig {
    private long pollInterval = 10000; //poll interval in milliseconds
    private int maxJobsActive = 32; // maximum number of open jobs
    private long timeout = 30 * 1000; // time to keep a poll open
    
    public long getPollInterval() {
        return pollInterval;
    }
    public void setPollInterval(long pollInterval) {
        this.pollInterval = pollInterval;
    }
    public int getMaxJobsActive() {
        return maxJobsActive;
    }
    public void setMaxJobsActive(int maxJobsActive) {
        this.maxJobsActive = maxJobsActive;
    }
    public long getTimeout() {
        return timeout;
    }
    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }
}
