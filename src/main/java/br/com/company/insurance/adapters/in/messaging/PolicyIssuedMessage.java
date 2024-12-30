package br.com.company.insurance.adapters.in.messaging;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PolicyIssuedMessage {
    @JsonProperty("quotation_id")
    private Long quotationId;
    @JsonProperty("policy_number")
    private Long policyNumber;
}
