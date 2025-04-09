package com.camunda.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateCustomerResponse {

    private Long customerId;
    private Integer annualIncome;
    private String employmentStatus;
}
