package com.camunda.demo.worker;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.camunda.demo.crud.ApplicationRepository;
import com.camunda.demo.model.AccountType;
import com.camunda.demo.model.Application;

import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.Variable;
import lombok.extern.java.Log;

@Component
@Log
public class ValidateDataWorker {

    @Autowired
    private ApplicationRepository applicationRepository;


   @JobWorker(type ="ro.bearingpoint.customerOnboarding.validateData")
    public Map<String, Object> validateData(@Variable Long applicationId) {

        log.info("validating data for application with id " + applicationId);

        boolean isValid = true;
        // Perform validation logic here

        Application application = applicationRepository.findById(applicationId).orElseThrow(() -> new RuntimeException("Application not found"));

        // run validation logic
        if (application.getFirstName() == null || application.getLastName() == null) {
            isValid = false;
        }

        // if accontType is unknown
        try{
            AccountType.fromDisplayName(application.getAccountType());
        }
        catch (IllegalArgumentException e){
            isValid = false;
        }


        return Map.of("dataIsValid", isValid);
    }

}
