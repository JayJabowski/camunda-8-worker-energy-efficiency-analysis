package envite.workertest;

import java.util.Map;

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
public class WorkerTestApplication implements CommandLineRunner {

	private final static Logger LOG = LoggerFactory.getLogger(WorkerTestApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(WorkerTestApplication.class, args);
	}

	@Autowired
	private ZeebeClientLifecycle client;

	@Override
	public void run(String... args) throws Exception {
		final ProcessInstanceEvent event = 
			client
			.newCreateInstanceCommand()
			.bpmnProcessId("workerGatewayId")
			.latestVersion()
			.variables(Map.of("message","Hello From WorkerTestApplication"))
			.send()
			.join();

			LOG.info("Started using "+event.getBpmnProcessId()+", "+event.getProcessInstanceKey());
			
	}

	

}
