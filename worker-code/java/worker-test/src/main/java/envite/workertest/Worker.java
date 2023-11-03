package envite.workertest;

import org.springframework.stereotype.Component;

import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.client.api.response.ActivatedJob;

@Component
public class Worker{

    public void consolePrintWorker(final ActivatedJob job){
        System.out.println("consolePrintWorker called "+job.toString());
    }
    
    @JobWorker(type= "workerTestGateway")
    public void workerTestGateway(final ActivatedJob job){
        consolePrintWorker(job);
    }
}