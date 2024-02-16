package jabowski.restworkerjava.worker;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.client.api.worker.JobHandler;
import jabowski.restworkerjava.entities.TypicalPayload;
import jabowski.restworkerjava.service.JobCounterService;
import jabowski.restworkerjava.service.TypicalPayloadMapper;
import lombok.Getter;
import lombok.Setter;
import reactor.core.publisher.Flux;

@Component
@Getter
@Setter
public class FetchJSONHandlerReactive implements JobHandler {
    private final static Logger LOG = LoggerFactory.getLogger(FetchJSONHandlerReactive.class);

    @Value("${test.responder.address:localhost:8090}") 
    String baseURL;

    @Value("${config.send.full.json.to.zeebe}")
    Boolean sendFullJSON;

    private int count = 0;

    JobCounterService counter;

    @Autowired
    ObjectMapper objectMapper;

    public FetchJSONHandlerReactive(JobCounterService counter){
        this.counter = counter;
    }

    @Override
    public void handle(JobClient client, ActivatedJob job) throws Exception {
        try{

            counter.incrementJobsStarted();
            LOG.info("Invoke REST call "+ count + ": ");
            LOG.info(job.toString());
            
            Flux<String> getJSONResponseFlux = WebClient.create()
            .get().uri("http://"+baseURL+"/testJSON")
            .retrieve()
            .bodyToFlux(String.class);
            
            
                // non-blocking, so we register the callbacks (for happy and exceptional case)
                getJSONResponseFlux.subscribe(
                    response -> {
                        
                        Map<String, Object> processVariables = sendFullJSON ? convertResponseToMap(response) : Map.of("var2", true);
                        
                        client.newCompleteCommand(job.getKey()).variables(processVariables).send()
                        // non-blocking, so we register the callbacks (for happy and exceptional case)
                        .thenApply(jobResponse -> { 
                        
                            LOG.info("Sampling response " + count++ + ". Received: " + jobResponse);
                            counter.incrementJobsFinished();
                            return jobResponse;
                            
                        })
                        .exceptionally(t -> {throw new RuntimeException("Could not complete job: " + t.getMessage(), t);});
                        
                    },
                    exception -> {
                        client.newFailCommand(job.getKey())
                        .retries(1)
                        .errorMessage("Could not invoke REST API: " + exception.getMessage()).send()
                        .exceptionally(t -> {throw new RuntimeException("Could not fail job: " + t.getMessage(), t);});
                    }
                    );
            }catch(Exception e){
                e.printStackTrace();
            }
            }

    private Map<String, Object> convertResponseToMap(String response) {
        try {
            TypicalPayload payload = objectMapper.readValue(response, TypicalPayload.class);

            return TypicalPayloadMapper.mapPayload(payload);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
    
