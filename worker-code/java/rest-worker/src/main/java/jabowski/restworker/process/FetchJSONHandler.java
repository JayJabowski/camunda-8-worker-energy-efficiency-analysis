package jabowski.restworker.process;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.client.api.worker.JobHandler;
import jabowski.restworker.rest.RestCallApi;

@Component
public class FetchJSONHandler implements JobHandler {

    @Autowired
    RestCallApi api;

    @Override
    public void handle(JobClient client, ActivatedJob job) throws Exception {

        api.getTestJSON();

        System.out.println("Handled: "+ job.getKey() + " in Worker: " + job.getWorker() + "\n");
    }
    
}
