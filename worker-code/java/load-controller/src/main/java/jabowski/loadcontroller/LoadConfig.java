package jabowski.loadcontroller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@Data
public class LoadConfig {

    @Value("${config.sleepduration:5}") 
    private Integer sleepDuration; //sleep duration in seconds
    @Value("${config.processname:benchmark_small}") 
    private String processName;
    @Value("${config.instancecount:-1}") 
    private Integer count;
    @Value("${config.parallelinstances:1}")
    private Integer parallelInstances;  
    
    }
