package jabowski.loadcontroller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadConfig {

    private Integer sleepDuration = 5; //sleep duration in seconds
    private String processName = "benchmark_small";
    
    public LoadConfig(
        @Value("${sleep.duration:5}") Integer sleepDuration,
        @Value("${process.name:benchmark_small}") String processName
        ){
            this.sleepDuration = (Integer) sleepDuration;
            this.processName = processName;
        }

    public Integer getSleepDuration() {
        return sleepDuration;
    }

    public void setSleepDuration(Integer sleepDuration) {
        this.sleepDuration = sleepDuration;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }
    }
