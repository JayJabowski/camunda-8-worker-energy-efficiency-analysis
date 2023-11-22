package jabowski.loadcontroller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@Getter
@Setter
public class LoadConfig {

    private Integer sleepDuration; //sleep duration in seconds
    private String processName;
    private Integer count;
    private Integer parallelInstances;
    
    public LoadConfig(
        @Value("${sleep.duration:5}") Integer sleepDuration,
        @Value("${process.name:benchmark_small}") String processName,
        @Value("${instance.count:-1}") Integer count,
        @Value("${parallel.instances:1}") Integer parallelInstances
        ){
            this.sleepDuration = sleepDuration;
            this.processName = processName;
            this.count = count;
            this.parallelInstances = parallelInstances;
        }
    }
