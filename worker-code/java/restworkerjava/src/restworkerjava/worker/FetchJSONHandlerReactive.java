package jabowski.restworkerjava.worker;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.client.api.worker.JobHandler;
import jabowski.restworkerjava.entities.TypicalPayload;
import jabowski.restworkerjava.rest.RestCallApi;
import lombok.Getter;
import lombok.Setter;
import reactor.core.publisher.Flux;

@Component
@Getter
@Setter
public class FetchJSONHandlerReactive implements JobHandler {
    private final static Logger LOG = LoggerFactory.getLogger(FetchJSONHandlerReactive.class);

    private int count = 0;

    RestCallApi api;

    public FetchJSONHandlerReactive(RestCallApi api){
        this.api = api;
    }

    @Override
    public void handle(JobClient client, ActivatedJob job) throws Exception {
        LOG.info("Invoke REST call "+ count + ": ");

        Flux<String> getJSONResponseFlux = WebClient.create()
            .get().uri(api.getBaseURL()+"/testJSON")
            .retrieve()
            .bodyToFlux(String.class);

          
        // non-blocking, so we register the callbacks (for happy and exceptional case)
        getJSONResponseFlux.subscribe(
            response -> {
            
            client.newCompleteCommand(job.getKey()).variables(Map.of("var2", true)).send()
                // non-blocking, so we register the callbacks (for happy and exceptional case)
                .thenApply(jobResponse -> { 
                    
                    LOG.info("Sampling response " + count++ + ". Received: " + jobResponse);
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
        }


    }
    
