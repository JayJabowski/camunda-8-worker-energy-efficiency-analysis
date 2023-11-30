package jabowski.restworkerjava.controller;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.ZeebeClientBuilder;
import io.camunda.zeebe.client.api.worker.JobHandler;
import jabowski.restworkerjava.worker.updateableWorkerWrapper;
import jakarta.annotation.PostConstruct;

@Component
public class WorkerController {

    //holds the Worker-Config in use against the worker's hash
    Map<String,updateableWorkerWrapper> workers = new ConcurrentHashMap<String, updateableWorkerWrapper>();
    
    @Value("${zeebe.client.broker.gatewayAddress:localhost:26500}") 
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

    updateableWorkerWrapper wrapper;

    @PostConstruct
    public void init(){
        final ZeebeClientBuilder builder = ZeebeClient.newClientBuilder().gatewayAddress(gatewayAddress).usePlaintext();
        client = builder.build();
    }

    //Entry Point: Collect Job Type and Handler, add to Map and Start Worker
    public void initializeWorker(String jobType, JobHandler handler){
        
        saveWorker(jobType, handler).startWorker();

    }

    //Save current config for job Type
    private updateableWorkerWrapper saveWorker(String jobType, JobHandler handler){
        updateableWorkerWrapper workerWrapper = createWorkerWrapper(jobType, handler);

        workers.put(
            jobType,
            workerWrapper);
        
        return workerWrapper;
    }

    private updateableWorkerWrapper createWorkerWrapper(String jobType, JobHandler handler){
        return new updateableWorkerWrapper()
                .setJobHandler(handler)
                .setJobType(jobType)
                .setClient(client)
                .setMaxJobsActive(maxJobsActive)
                .setPollInterval(pollInterval)
                .setTimeout(timeout)
                .setRequestTimeout(requestTimeout);
    }
    
}
