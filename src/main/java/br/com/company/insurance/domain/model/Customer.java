package br.com.company.insurance.domain.model;

import lombok.*;

import java.time.LocalDate;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    private String documentNumber;
    private String name;
    private String type;
    private String gender;

    private LocalDate dateOfBirth;
    private String email;
    private String phoneNumber;
}
