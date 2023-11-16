package jabowski.restworkerjava.worker;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.client.api.worker.JobHandler;
import jabowski.restworkerjava.entities.TypicalPayload;
import jabowski.restworkerjava.rest.RestCallApi;

@Component
public class FetchJSONHandler implements JobHandler{

    private final static Logger LOG = LoggerFactory.getLogger(FetchJSONHandler.class);
    private static int count = 0;

    RestCallApi api;

    public FetchJSONHandler(RestCallApi api){
        this.api=api;
    }

    public RestCallApi getApi() {
        return api;
    }

    public void setApi(RestCallApi api) {
        this.api = api;
    }

    @Override
    public void handle(JobClient client, ActivatedJob job) throws Exception {
        
        LOG.info("Handling: " + job + " with " + client + "\n");

        TypicalPayload response = api.getTestJSON();

        
        LOG.info("Sampling response " + count++ + ". Received var1: "+ response.getVar1() + ", var3:" + response.getVar3() + ", var5:" + response.getVar5());
        client
            .newCompleteCommand(job.getKey())
            .variables(Map.of("var2", true))
            .send()
            .join();

    }

}