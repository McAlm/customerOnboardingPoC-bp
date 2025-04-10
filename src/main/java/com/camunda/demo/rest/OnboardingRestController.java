package com.camunda.demo.rest;

import java.time.ZoneOffset;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.camunda.demo.crud.ApplicationRepository;
import com.camunda.demo.model.Application;

import io.camunda.zeebe.client.ZeebeClient;
import lombok.extern.java.Log;

@RestController
@Log
public class OnboardingRestController {

    @Autowired
    private ZeebeClient zeebeClient;

    @Autowired
    private ApplicationRepository applicationRepository;

    @PostMapping("/newApplication")
    public Map<String, Object> newApplication(@RequestBody ApplicationRequestData ard) {

        log.info("Received application: " + ard.personal_details().first_name() + " "
                + ard.personal_details().last_name());
        // save the application to the database ==> return the application ID

        Application application = new Application();
        application.setFirstName(ard.personal_details().first_name());
        application.setLastName(ard.personal_details().last_name());
        application.setDateOfBirth(
                java.util.Date.from(ard.personal_details().date_of_birth().atStartOfDay().toInstant(ZoneOffset.UTC)));
        application.setAccountType(ard.account_details().account_type());
        application.setCurrency(ard.account_details().currency());
        application.setStreet(ard.contact_details().address().street());
        application.setCity(ard.contact_details().address().city());
        application.setState(ard.contact_details().address().state());
        application.setPostalCode(ard.contact_details().address().postal_code());
        application.setCountry(ard.contact_details().address().country());
        application.setEmail(ard.contact_details().email());
        application.setPhone(ard.contact_details().phone());
        application.setAnnualIncome(ard.employment_details().annual_income());
        application.setEmploymentStatus(ard.employment_details().status());

        Long id = applicationRepository.save(application).getId();

        // send the application to the process engine
        zeebeClient.newCreateInstanceCommand()
                .bpmnProcessId("customerOnboarding")
                .latestVersion()
                .variables(Map.of("applicationId", id, "customerEmail", ard.contact_details().email()))
                .send()
                .join();

        return Map.of("success", true, "applicationId", id);
    }

    @DeleteMapping("/application/cancel")
    @ResponseBody
    public Map<String, Object> cancelApplication(@RequestParam String applicationId) {
        log.info("Received cancel application request for application ID: " + applicationId);
        zeebeClient.newPublishMessageCommand()
                .messageName("MSG_APPLICATION_CANCELLED")
                .correlationKey(applicationId)
                .send()
                .join();
        return Map.of("applicationId", applicationId, "status", "cancelled");
    }

}
