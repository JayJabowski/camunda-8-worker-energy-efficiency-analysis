package jabowski.loadcontroller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;

@SpringBootApplication
public class LoadControllerApplication implements CommandLineRunner {

	private int counter = 0;

	private final static Logger LOG = LoggerFactory.getLogger(LoadControllerApplication.class);

	@Autowired
	ZeebeClient client;

	@Autowired
	LoadConfig config;

	public static void main(String[] args) {
		SpringApplication.run(LoadControllerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		while(	
			counter < config.getCount()
			||
			config.getCount() < 0 // run indefinitely if env INSTANCE_COUNT set to -1
			){
			ProcessInstanceEvent event = 
				client
					.newCreateInstanceCommand()
					.bpmnProcessId(config.getProcessName())
					.latestVersion()
					.send()
					.join();
			LOG.info("Started Instance "+ counter++ +" using "+event.getBpmnProcessId()+", "+event.getProcessInstanceKey());

			Thread.sleep(config.getSleepDuration() * 1000);
		}
	}

}
