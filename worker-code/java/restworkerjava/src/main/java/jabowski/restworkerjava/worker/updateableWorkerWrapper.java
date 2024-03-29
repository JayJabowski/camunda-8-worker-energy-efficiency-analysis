package jabowski.restworkerjava.worker;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.worker.JobHandler;
import io.camunda.zeebe.client.api.worker.JobWorker;
import jabowski.restworkerjava.events.ConfigUpdateEvent;
import lombok.NoArgsConstructor;

@Configuration
@NoArgsConstructor
public class updateableWorkerWrapper {

    private Long timeout; // time to keep a poll open
    private Long pollInterval; //poll interval in seconds
    private Integer maxJobsActive; // maximum number of open jobs
    private Long requestTimeout;

    private String jobType;
     
    private JobHandler jobHandler; // Name of the class to be created as a handler eg "FetchJSONHandler.class"
    private JobWorker worker; // reference to actual worker
	private	ZeebeClient client;

    
    private final static Logger LOG = LoggerFactory.getLogger(updateableWorkerWrapper.class);
    
    //GETTER SETTER
    public Long getRequestTimeout() {
        return requestTimeout;
    }
    public updateableWorkerWrapper setRequestTimeout(Long requestTimeout) {
        this.requestTimeout = requestTimeout;
        return this;
    }
    public ZeebeClient getClient() {
        return client;
    }
    public updateableWorkerWrapper setClient(ZeebeClient client) {
        this.client = client;
        return this;
    }
    public JobHandler getJobHandler() {
        return jobHandler;
    }
    public updateableWorkerWrapper setJobHandler(JobHandler jobHandler) {
        this.jobHandler = jobHandler;
        return this;
    }
    public long getPollInterval() {
        return pollInterval;
    }
    public updateableWorkerWrapper setPollInterval(long pollInterval) {
        this.pollInterval = pollInterval;
        return this;
    }
    public int getMaxJobsActive() {
        return maxJobsActive;
    }
    public updateableWorkerWrapper setMaxJobsActive(int maxJobsActive) {
        this.maxJobsActive = maxJobsActive;
        return this;
    }
    public long getTimeout() {
        return timeout;
    }
    public updateableWorkerWrapper setTimeout(long timeout) {
        this.timeout = timeout;
        return this;
    }
    public String getJobType() {
        return jobType;
    }
    public updateableWorkerWrapper setJobType(String jobType) {
        this.jobType = jobType;
        return this;
    }
    
    //LOGIC
    public void startWorker(){
        try{            
            worker = client
                .newWorker()
                .jobType(jobType)
                .handler(jobHandler)
                .name(jobType)
                .pollInterval(
                    Duration.ofSeconds(pollInterval))
                .maxJobsActive(maxJobsActive)
                .timeout(timeout) // How long may the worker take to finish job
                .requestTimeout(Duration.ofSeconds(requestTimeout)) // how long will the request be kept open, -1 to deactivate long polling
                .open();

            LOG.info("\n\tOpening Type: " + jobType + " with " + jobHandler 
                + "\n\t maxJobsActive = " + maxJobsActive 
                + "\n\t pollInterval = " + pollInterval
                + "\n\t timeout = " + timeout
                + "\n\t requestTimeout = " + requestTimeout);

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    //not in use at the moment
    private void closeWorker(){
        if(worker.isOpen()) worker.close();
        LOG.info("Closing Type:" + jobType + "\n");
    }

    //not in use at the moment
    private void restartWorker(){
        closeWorker();
        startWorker();
    }

    //not in use at the moment, worker can not be updated while running
    public void updateConfig(ConfigUpdateEvent e){
        if(e.getPollInterval() != null) pollInterval = e.getPollInterval();
        if(e.getTimeout() != null) timeout = e.getTimeout();
        if(e.getMaxJobsActive() != null) maxJobsActive = e.getMaxJobsActive();

        LOG.info("Changed config found: Using max " + maxJobsActive +" jobs, pollInterval " + pollInterval + " ms, timeout " + timeout + " ms. \n");

        restartWorker();
    }
    

}
