package br.com.company.insurance.adapters.in.rest.mappers;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CustomerRequest {

    @JsonProperty("document_number")
    private String documentNumber;

    private String name;
    private String type;
    private String gender;

    @JsonProperty("date_of_birth")
    private String dateOfBirth;

    private String email;

    @JsonProperty("phone_number")
    private String phoneNumber;
}
