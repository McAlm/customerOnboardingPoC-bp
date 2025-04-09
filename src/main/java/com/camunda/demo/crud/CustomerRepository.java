package com.camunda.demo.crud;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.camunda.demo.model.Customer;


public interface CustomerRepository extends CrudRepository<Customer, Long> {


    public Optional<Customer> findByFirstNameAndLastNameAndDateOfBirth(String firstName, String lastName, Date dateOfBirth);

}
