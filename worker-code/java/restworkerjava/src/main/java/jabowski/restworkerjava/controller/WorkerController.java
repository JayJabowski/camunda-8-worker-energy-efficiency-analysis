package jabowski.restworkerjava.controller;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.worker.JobHandler;
import io.camunda.zeebe.client.api.worker.JobWorker;
import jabowski.restworkerjava.config.WorkerConfig;
import jabowski.restworkerjava.config.localWorkerConfig;

@Component
public class WorkerController {

    //holds the Worker-Config in use against the worker's hash
    Map<Integer,localWorkerConfig> workerConfigs = new ConcurrentHashMap<Integer, localWorkerConfig>();

    //keeps track of Handlers associated with Job Types
    Map<String, JobHandler> handlerMap = new ConcurrentHashMap<String, JobHandler>();
    
    @Autowired
    WorkerConfig globalConfig;

    @Autowired
    ZeebeClient client;

    //Entry Point: Collect Job Type and Handler, add to Map and Start Worker
    public void initializeWorker(String jobType, JobHandler handler){
        if(!handlerMap.containsKey(jobType)){
            handlerMap.put(jobType, handler);
        }

        startWorkerWithCurrentConfig(jobType);

    }

    public void startWorkerWithCurrentConfig(String jobType){
    
        JobWorker worker =  client
            .newWorker()
            .jobType(jobType)
            .handler(handlerMap.get(jobType))
            .pollInterval(
                Duration.ofSeconds(globalConfig.getPollInterval()))
            .maxJobsActive(globalConfig.getMaxJobsActive())
            .timeout(globalConfig.getTimeout())
            .open();
                
        registerWorkerConfig(worker, jobType);
    }

    // adds config used for worker to HashMap
    public void registerWorkerConfig(JobWorker worker, String jobType){
        workerConfigs.put(
            worker.hashCode(),
            new localWorkerConfig()
                .setMaxJobsActive(globalConfig.getMaxJobsActive()))
                .setPollInterval(globalConfig.getPollInterval())
                .setTimeout(globalConfig.getTimeout())
                .setJobType(jobType);
    }

    // checks if applied Config is still current
    public boolean isCurrentConfig(JobWorker worker){
        localWorkerConfig localConfig = workerConfigs.get(worker.hashCode());

        if(
            globalConfig.getMaxJobsActive() != localConfig.getMaxJobsActive()
            ||
            globalConfig.getPollInterval() != localConfig.getPollInterval()
            ||
            globalConfig.getTimeout() != localConfig.getTimeout()
            ) return false;

        return true;
    }

    // reloads worker if new config needs to be aplied
    public void reloadWorker(JobWorker worker){

        //TODO: Is this necessary?
        if(worker.isClosed()) return;

        int hash = worker.hashCode();

        //get jobType from Config
        String jobType = workerConfigs.get(hash)
                                        .getJobType();

        //close old worker
        worker.close();
        
        //delete Config from Map
        workerConfigs.remove(hash);

        //start new Worker with current config
        startWorkerWithCurrentConfig(jobType);
    }

    public void checkForReload(JobWorker worker){

        //if everything is up-to-date - do nothing
        if(isCurrentConfig(worker)) return;

        reloadWorker(worker);
    }

    /*
     public class ConfigChangeListener{
         
         @Subscribe
         public void configChanged(ConfigUpdateEvent configUpdate){
             // update all workers
             
            }
            
            
        }
    */
}
