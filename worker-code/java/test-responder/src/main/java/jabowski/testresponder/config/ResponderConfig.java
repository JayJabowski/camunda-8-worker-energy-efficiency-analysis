package jabowski.testresponder.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@Data
public class ResponderConfig {

      //time to wait before responding
    @Value("${config.responsedelay:100}") 
    private Integer waitTime;

}
