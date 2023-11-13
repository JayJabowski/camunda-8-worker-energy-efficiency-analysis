package jabowski.loadcontroller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import io.camunda.zeebe.spring.client.EnableZeebeClient;
import io.camunda.zeebe.spring.client.config.ZeebeClientStarterAutoConfiguration;
import io.camunda.zeebe.spring.client.lifecycle.ZeebeClientLifecycle;

@SpringBootApplication
@EnableZeebeClient
@Import(ZeebeClientStarterAutoConfiguration.class)
public class LoadControllerApplication implements CommandLineRunner {

	//Configure hard-coded benchmark parameters here
	//TODO: Have parameters adjustable through API
	private final int SLEEP_DURATION = 5; //sleep duration in seconds
	private final String PROCESS_NAME = "benchmark-small";

	private int counter = 0;

	private final static Logger LOG = LoggerFactory.getLogger(LoadControllerApplication.class);

	@Autowired
	ZeebeClientLifecycle client;


	public static void main(String[] args) {
		SpringApplication.run(LoadControllerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		while(true){
			final ProcessInstanceEvent event = 
			client
			.newCreateInstanceCommand()
			.bpmnProcessId(PROCESS_NAME)
			.latestVersion()
			.send()
			.join();
			LOG.info("Started Instance "+ counter++ +" using "+event.getBpmnProcessId()+", "+event.getProcessInstanceKey());

			Thread.sleep(SLEEP_DURATION * 1000);
		}

		 
		

	}

}
