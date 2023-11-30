package jabowski.restworker.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;
import org.springframework.web.reactive.function.client.WebClient.UriSpec;

import reactor.core.publisher.Mono;

@Service
public class RestCallApi {
	    
    WebClient client;

    public RestCallApi(@Value("${test.responder.address}") String baseURL){
        //Remember: Env-Vars are not case-sensitive, also replace '_' with '.'

        // Uncomment/Replace for local setup
        //baseURL = "http://127.0.0.1:8090";

        System.out.println("\n Constructor says: " + baseURL + "\n\n");
        client = WebClient.create("http://"+baseURL);

    }

    public String getTestJSON(){

        // Specify HTTP-Method
        UriSpec<RequestBodySpec> uriSpec = client.method(HttpMethod.GET);

        //provide URL
        RequestBodySpec bodySpec = uriSpec.uri("/testJSON");

        //Defining how to store the data
        RequestHeadersSpec<?> headersSpec = bodySpec.bodyValue("data");

        //Making the request
        Mono<String> response = headersSpec.retrieve()
        .bodyToMono(String.class);

        return response.block();
    }

    
    
}
