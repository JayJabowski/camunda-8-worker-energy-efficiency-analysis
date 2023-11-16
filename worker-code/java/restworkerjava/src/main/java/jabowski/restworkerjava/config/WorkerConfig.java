package jabowski.restworkerjava.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
public class WorkerConfig {
    private Long pollInterval; //poll interval in milliseconds
    private Integer maxJobsActive; // maximum number of open jobs
    private Long timeout; // time to keep a poll open

    public WorkerConfig(
        @Value("${max.jobs.active:24}") Integer maxJobsActive, 
        @Value("${poll.interval:2L}") Long pollInterval,
        @Value("${timeout:60000L}") Long timeout
        ){
            this.maxJobsActive = maxJobsActive;
            this.pollInterval = pollInterval;
            this.timeout = timeout;
        }
    
}
