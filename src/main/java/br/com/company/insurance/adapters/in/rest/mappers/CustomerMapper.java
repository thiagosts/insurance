package br.com.company.insurance.adapters.in.rest.mappers;

import br.com.company.insurance.domain.model.Customer;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class CustomerMapper {

    public Customer toDomain(CustomerRequest request) {
        if (request == null) {
            return null;
        }

        return Customer.builder()
                .documentNumber(request.getDocumentNumber())
                .name(request.getName())
                .type(request.getType())
                .gender(request.getGender())
                .dateOfBirth(LocalDate.parse(request.getDateOfBirth())) // ou parse se for String
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .build();
    }

    public CustomerResponse toResponse(Customer customer) {
        if (customer == null) {
            return null;
        }
        CustomerResponse response = new CustomerResponse();
        response.setDocumentNumber(customer.getDocumentNumber());
        response.setName(customer.getName());
        response.setType(customer.getType());
        response.setGender(customer.getGender());
        response.setDateOfBirth(String.valueOf(customer.getDateOfBirth()));
        response.setEmail(customer.getEmail());
        response.setPhoneNumber(customer.getPhoneNumber());
        return response;
    }
}
