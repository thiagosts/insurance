package br.com.company.insurance.application;

import br.com.company.insurance.domain.model.Quotation;

public interface CreateQuotationPortIn {
    Quotation create(Quotation quotation);
}
