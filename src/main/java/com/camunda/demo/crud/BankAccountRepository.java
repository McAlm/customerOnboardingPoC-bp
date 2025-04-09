package com.camunda.demo.crud;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.camunda.demo.model.BankAccount;

public interface BankAccountRepository extends CrudRepository<BankAccount, Long> {

    List<BankAccount> findByCustomerId(Long customerId);

}
