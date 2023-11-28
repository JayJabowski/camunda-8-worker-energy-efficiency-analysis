package jabowski.restworkerjava.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;
import org.springframework.web.reactive.function.client.WebClient.UriSpec;

import jabowski.restworkerjava.RestworkerjavaApplication;
import jabowski.restworkerjava.entities.TypicalPayload;
import lombok.Getter;
import lombok.Setter;
import reactor.core.publisher.Mono;

@Service
@Getter
@Setter
public class RestCallApi {

    private final static Logger LOG = LoggerFactory.getLogger(RestCallApi.class);
	    
    WebClient client;
    // private String baseURL = "127.0.0.1:8090";
    String baseURL;

    public RestCallApi(@Value("${test.responder.address:localhost:8090}") String baseURL){
        if(!baseURL.startsWith("http://")){
            this.baseURL = "http://"+baseURL;
        }
        else{
            this.baseURL = baseURL;
        }

        LOG.info("Using baseURL = "+this.baseURL);
        //Remember: Env-Vars are not case-sensitive, also replace '_' with '.'

        client = WebClient.create(this.baseURL);
    }

    public TypicalPayload getTestJSON(){

        // Specify HTTP-Method
        UriSpec<RequestBodySpec> uriSpec = client.method(HttpMethod.GET);

        //provide URL
        RequestBodySpec bodySpec = uriSpec.uri("/testJSON");

        //Defining how to store the data
        RequestHeadersSpec<?> headersSpec = bodySpec.bodyValue("data");

        //Making the request
        Mono<TypicalPayload> response = headersSpec.retrieve()
        .bodyToMono(TypicalPayload.class);

        return response.block();
    }

    
    
}
