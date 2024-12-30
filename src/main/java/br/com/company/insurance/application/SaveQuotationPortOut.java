package br.com.company.insurance.application;

import br.com.company.insurance.domain.model.Quotation;

public interface SaveQuotationPortOut {

    Quotation save(Quotation quotation);

    void updatePolicyNumber(Long quotationId, Long policyNumber);
}
