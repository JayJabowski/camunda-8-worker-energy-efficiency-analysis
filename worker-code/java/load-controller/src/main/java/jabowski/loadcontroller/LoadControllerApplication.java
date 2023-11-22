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
	private int parallel_counter = 0;

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

			while(parallel_counter < config.getParallelInstances()){

				ProcessInstanceEvent event = 
				client
				.newCreateInstanceCommand()
				.bpmnProcessId(config.getProcessName())
				.latestVersion()
				.send()
				.join();
				LOG.info("Started " + parallel_counter++ + " of " + config.getParallelInstances() +  " in run "+ (counter++ % config.getCount()) +" using "+event.getBpmnProcessId()+", "+event.getProcessInstanceKey());
			}

			parallel_counter = 0;

			Thread.sleep(config.getSleepDuration() * 1000);
		}
	}

}
