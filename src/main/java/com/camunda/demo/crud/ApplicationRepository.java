package com.camunda.demo.crud;

import org.springframework.data.repository.CrudRepository;

import com.camunda.demo.model.Application;



public interface ApplicationRepository extends CrudRepository<Application, Long> {


}
