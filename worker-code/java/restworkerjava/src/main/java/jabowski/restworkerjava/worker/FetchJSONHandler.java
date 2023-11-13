package jabowski.restworkerjava.worker;

import org.springframework.beans.factory.annotation.Autowired;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.client.api.worker.JobHandler;
import jabowski.restworkerjava.controller.WorkerController;
import jabowski.restworkerjava.rest.RestCallApi;

public class FetchJSONHandler implements JobHandler{

    @Autowired
    WorkerController controller;

    @Autowired
    RestCallApi api;

    @Override
    public void handle(JobClient client, ActivatedJob job) throws Exception {
        
        String res = api.getTestJSON();
                    
        System.out.println(job);
        client
            .newCompleteCommand(job.getKey())
            .send()
            .join();

    }

    }