package jabowski.restworkerspring.worker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;




@Component
public class FetchJSONHandler{

    private final static Logger LOG = LoggerFactory.getLogger(FetchJSONHandler.class);

    @JobWorker(type = "fetch-json")
    public void fetchJSON(JobClient client, ActivatedJob job) throws Exception {
        
        LOG.info("Handling: " + job + " with " + client + "\n");

        //String res = api.getTestJSON();

        LOG.info("Received: ");
                    
        System.out.println(job);
        client
            .newCompleteCommand(job.getKey())
            .send()
            .join();

    }

}