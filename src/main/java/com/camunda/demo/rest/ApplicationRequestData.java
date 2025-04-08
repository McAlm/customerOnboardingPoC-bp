package com.camunda.demo.rest;

import java.time.LocalDate;

public record ApplicationRequestData(
    String application_id,
    LocalDate application_date,
    PersonalDetails personal_details,
    ContactDetails contact_details,
    EmploymentDetails employment_details,
    AccountDetails account_details,
    Consent consent
) {
    public record PersonalDetails(
        String first_name,
        String last_name,
        LocalDate date_of_birth,
        String gender,
        String nationality,
        Identification identification
    ) {
        public record Identification(
            String type,
            String number,
            LocalDate issue_date,
            LocalDate expiry_date
        ) {}
    }

    public record ContactDetails(
        String email,
        String phone,
        Address address
    ) {
        public record Address(
            String street,
            String city,
            String state,
            String postal_code,
            String country
        ) {}
    }

    public record EmploymentDetails(
        String status,
        String employer_name,
        String job_title,
        int annual_income
    ) {}

    public record AccountDetails(
        String account_type,
        String currency,
        int initial_deposit,
        boolean debit_card_required,
        boolean online_banking_required
    ) {}

    public record Consent(
        boolean terms_and_conditions_accepted,
        boolean marketing_consent
    ) {}
}