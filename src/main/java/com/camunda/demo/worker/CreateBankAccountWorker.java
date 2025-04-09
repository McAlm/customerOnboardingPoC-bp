package com.camunda.demo.worker;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.camunda.demo.crud.ApplicationRepository;
import com.camunda.demo.crud.BankAccountRepository;
import com.camunda.demo.crud.CustomerRepository;
import com.camunda.demo.model.Application;
import com.camunda.demo.model.BankAccount;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.Variable;
import io.camunda.zeebe.spring.common.exception.ZeebeBpmnError;

@Component
public class CreateBankAccountWorker {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @JobWorker(type = "ro.bearingpoint.customerOnboarding.createBankAccount", //
            fetchVariables = { "applicationId", "customerId" }, autoComplete = false)
    public void createBankAccount(ActivatedJob job, JobClient client, @Variable Long applicationId,
            @Variable Long customerId) {
        // get the application
        Application app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        // fetch existing bank accounts for customerId
        List<BankAccount> bankAccounts = bankAccountRepository.findByCustomerId(customerId);

        if (app.getAccountType().equals("Checking")) {
            // check if there is already a checking account for the customer
            for (BankAccount bankAccount : bankAccounts) {
                if (bankAccount.getAccountType().equals("Checking")) {
                    // throw an BPMNError

                    throw new ZeebeBpmnError("CHECKING_ACCOUNT_EXISTS", "Customer already has a checking account",
                            null);
                }
            }
        }

        // create a new bank account
        BankAccount bankAccount = new BankAccount();
        bankAccount.setAccountType(app.getAccountType());
        bankAccount.setAccountNumber(UUID.randomUUID().toString());
        bankAccount.setCustomer(
                customerRepository.findById(customerId).orElseThrow(() -> new RuntimeException("Customer not found")));

        bankAccountRepository.save(bankAccount);

        // complete the job
        client.newCompleteCommand(job.getKey())
                .send()
                .join();

    }

}
