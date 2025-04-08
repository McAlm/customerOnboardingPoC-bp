package com.camunda.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.spring.client.annotation.Deployment;
import lombok.extern.java.Log;

@SpringBootApplication
@Log
//@Deployment(resources = { "classpath:bpmn/doNothing.bpmn" })
public class BlankDemoApplication implements CommandLineRunner {

	@Autowired
	private ZeebeClient zeebe;

	public static void main(String[] args) {
		SpringApplication.run(BlankDemoApplication.class, args);
	}

	public void run(final String... args) {
		// log.info("start process instance...");
		// zeebe.newCreateInstanceCommand().bpmnProcessId("DoNothing").latestVersion().variable("someVar", 1).send()
		// 		.join();
		// log.info("process instance started...");
	}
}
