package jabowski.restworkerjava.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.ZeebeClientBuilder;
import io.camunda.zeebe.client.api.worker.JobHandler;
import jabowski.restworkerjava.worker.updateableWorkerWrapper;
import jakarta.annotation.PostConstruct;

@Component
public class WorkerController {
   
    @Value("${zeebe.client.broker.gatewayAddress}") 
    String gatewayAddress;
    @Value("${config.timeout}") 
    Long timeout; // time to keep a poll open
    @Value("${config.pollinterval}") 
    Long pollInterval; //poll interval in seconds
    @Value("${config.maxjobsactive}") 
    Integer maxJobsActive; // maximum number of open jobs
    @Value("${config.requesttimeout}") 
    Long requestTimeout; // how long to keep request open, -1 to deactivate long polling, 0 for zeebe default value

    ZeebeClient client;

    @Autowired
    updateableWorkerWrapper wrapper;

    @PostConstruct
    public void init(){
        final ZeebeClientBuilder builder = ZeebeClient.newClientBuilder().gatewayAddress(gatewayAddress).usePlaintext();
        client = builder.build();
    }

    //Create and Start Worker
    public void initializeWorker(String jobType, JobHandler handler){

        wrapper
            .setJobHandler(handler)
            .setJobType(jobType)
            .setClient(client)
            .setMaxJobsActive(maxJobsActive)
            .setPollInterval(pollInterval)
            .setTimeout(timeout)
            .setRequestTimeout(requestTimeout);

        wrapper.startWorker();

    }

    
}
