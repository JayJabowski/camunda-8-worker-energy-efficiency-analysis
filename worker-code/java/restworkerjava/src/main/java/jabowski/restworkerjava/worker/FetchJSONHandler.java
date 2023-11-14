package jabowski.restworkerjava.worker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.client.api.worker.JobHandler;
import jabowski.restworkerjava.rest.RestCallApi;

@Component
public class FetchJSONHandler implements JobHandler{

    private final static Logger LOG = LoggerFactory.getLogger(FetchJSONHandler.class);

    @Autowired
    RestCallApi api;

    @Override
    public void handle(JobClient client, ActivatedJob job) throws Exception {
        
        LOG.info("Handling: " + job + " with " + client + "\n");

        String res = api.getTestJSON();

        LOG.info("Received: "+res);
                    
        System.out.println(job);
        client
            .newCompleteCommand(job.getKey())
            .send()
            .join();

    }

}