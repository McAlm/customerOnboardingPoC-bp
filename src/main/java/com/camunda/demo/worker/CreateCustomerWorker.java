package com.camunda.demo.worker;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.camunda.demo.crud.ApplicationRepository;
import com.camunda.demo.crud.CustomerRepository;
import com.camunda.demo.dto.CreateCustomerResponse;
import com.camunda.demo.model.Application;
import com.camunda.demo.model.Customer;

import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.Variable;

@Component
public class CreateCustomerWorker {


    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @JobWorker(type = "ro.bearingpoint.customerOnboarding.createCustomer",//
                 fetchVariables = {"applicationId"})
    public CreateCustomerResponse createCustomer(@Variable Long applicationId) {

        Customer customer = null;
        Application application = applicationRepository.findById(applicationId).orElseThrow(() -> new RuntimeException("Application not found"));
       
        Optional <Customer> custOpt = customerRepository.findByFirstNameAndLastNameAndDateOfBirth(application.getFirstName(), application.getLastName(), application.getDateOfBirth());

        if(custOpt.isPresent()){
            return new CreateCustomerResponse(custOpt.get().getId(), application.getAnnualIncome(), application.getEmploymentStatus());
        } else {
            customer = new Customer();
            customer.setFirstName(application.getFirstName());
            customer.setLastName(application.getLastName());
            customer.setDateOfBirth(application.getDateOfBirth());
            customer.setStreet(application.getStreet());
            customer.setCity(application.getCity());
            customer.setState(application.getState());
            customer.setPostalCode(application.getPostalCode());
            customer.setCountry(application.getCountry());
            customer.setEmail(application.getEmail());
            customer.setPhone(application.getPhone());
            customer.setCreatedDate(new Date());

            customer = customerRepository.save(customer);
            return new CreateCustomerResponse(customer.getId(), application.getAnnualIncome(), application.getEmploymentStatus());
        }

    }
}
