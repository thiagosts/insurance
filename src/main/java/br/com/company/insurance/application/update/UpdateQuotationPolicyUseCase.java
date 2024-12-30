package br.com.company.insurance.application.update;

public interface UpdateQuotationPolicyUseCase {
    void updatePolicyNumber(Long quotationId, Long policyNumber);
}
