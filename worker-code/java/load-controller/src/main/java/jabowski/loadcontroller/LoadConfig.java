package jabowski.loadcontroller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@Getter
@Setter
public class LoadConfig {

    private Integer sleepDuration = 5; //sleep duration in seconds
    private String processName = "benchmark_small";
    private Integer count;
    
    public LoadConfig(
        @Value("${sleep.duration:5}") Integer sleepDuration,
        @Value("${process.name:benchmark_small}") String processName,
        @Value("${instance.count:-1}") Integer count
        ){
            this.sleepDuration = (Integer) sleepDuration;
            this.processName = processName;
            this.count = count;
        }
    }
