package jabowski.restworkerjava.controller;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.ZeebeClientBuilder;
import io.camunda.zeebe.client.api.worker.JobHandler;
import jabowski.restworkerjava.worker.updateableWorkerWrapper;

@Component
public class WorkerController {

    //holds the Worker-Config in use against the worker's hash
    Map<String,updateableWorkerWrapper> workers = new ConcurrentHashMap<String, updateableWorkerWrapper>();
    
    ZeebeClient client;


    public WorkerController(@Value("${gateway.address}") String gatewayAddress){
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
                .setClient(client);
    }
    
}
