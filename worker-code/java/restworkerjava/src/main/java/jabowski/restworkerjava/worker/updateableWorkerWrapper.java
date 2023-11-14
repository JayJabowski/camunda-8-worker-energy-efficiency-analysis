package jabowski.restworkerjava.worker;

import java.time.Duration;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.eventbus.Subscribe;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.worker.JobHandler;
import io.camunda.zeebe.client.api.worker.JobWorker;
import jabowski.restworkerjava.events.ConfigUpdateEvent;
import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
public class updateableWorkerWrapper {

    private Long timeout = 1000 * 120L; // time to keep a poll open
    private Long pollInterval = 1000 * 20L; //poll interval in milliseconds
    private Integer maxJobsActive = 32; // maximum number of open jobs
    private JobHandler jobHandler; // Name of the class to be created as a handler eg "FetchJSONHandler.class"
    private JobWorker worker; // reference to actual worker
    private String jobType; 
	private	ZeebeClient client;

    private final static Logger LOG = LoggerFactory.getLogger(updateableWorkerWrapper.class);

    public updateableWorkerWrapper(
        @Value("${default.max.jobs.active}") Integer maxJobsActive, 
        @Value("${default.poll.interval}") Long pollInterval,
        @Value("${default.timeout}") Long timeout){

            this.maxJobsActive = maxJobsActive;
            this.pollInterval = pollInterval;
            this.timeout = timeout;
        
        LOG.info("Wrapper initialized: Using max " + maxJobsActive +" jobs, pollInterval " + pollInterval + " ms, timeout " + timeout + " ms. \n");
    }

    //GETTER SETTER
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
                .timeout(timeout)
                .open();

            LOG.info("Opening Type:" + jobType + " with " + jobHandler + "\n");

            //waitUntilSystemInput("exit");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private void closeWorker(){
        if(worker.isOpen()) worker.close();
        LOG.info("Closing Type:" + jobType + "\n");
    }

    private void restartWorker(){
        closeWorker();
        startWorker();
    }

    @Subscribe
    public void updateConfig(ConfigUpdateEvent e){
        if(e.getPollInterval() != null) pollInterval = e.getPollInterval();
        if(e.getTimeout() != null) timeout = e.getTimeout();
        if(e.getMaxJobsActive() != null) maxJobsActive = e.getMaxJobsActive();

        LOG.info("Changed config found: Using max " + maxJobsActive +" jobs, pollInterval " + pollInterval + " ms, timeout " + timeout + " ms. \n");

        restartWorker();
    }

    private static void waitUntilSystemInput(final String exitCode) {
        try (final Scanner scanner = new Scanner(System.in)) {
          while (scanner.hasNextLine()) {
            final String nextLine = scanner.nextLine();
            if (nextLine.contains(exitCode)) {
              return;
            }
          }
        }
      }
    

}
