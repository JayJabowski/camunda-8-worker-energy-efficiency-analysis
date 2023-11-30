package jabowski.restworker.process;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.VariablesAsType;
import io.camunda.zeebe.spring.client.lifecycle.ZeebeClientLifecycle;
import jabowski.restworker.rest.RestCallApi;
import io.camunda.zeebe.client.api.response.ActivatedJob;

@Component
public class RestWorker {

    @Autowired
    RestCallApi api;

    int a = 5;

    @Autowired
    private ZeebeClientLifecycle client;
    
    @JobWorker(type= "fetch-test-json")
    public void fetchTestJSON(@VariablesAsType ProcessVariables variables, final ActivatedJob job){
        String response = api.getTestJSON();

        System.out.println("\nCOUNT:" + variables.getCount() + "ID: "+ job.getElementId() + "\n");
        System.out.println("FETCHED: " + response.substring(0, 12) + "... \n");

        Map<String, Integer> outVariables = new HashMap<>();

        outVariables.put("count", variables.getCount() + 1);

        client.newCompleteCommand(job)
        .variables(outVariables)
        .send();
    }
    
}
