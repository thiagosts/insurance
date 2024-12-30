package br.com.company.insurance.adapters.in.rest.mappers;

import br.com.company.insurance.domain.model.Customer;
import br.com.company.insurance.domain.model.Quotation;
import org.springframework.stereotype.Component;

@Component
public class QuotationMapper {

    public Quotation toDomain(QuotationRequest request) {
        if (request == null) {
            return null;
        }
        Customer customerDomain = null;
        if (request.getCustomer() != null) {
            customerDomain = toDomain(request.getCustomer());
        }

        return Quotation.builder()
                .productId(request.getProductId())
                .offerId(request.getOfferId())
                .category(request.getCategory())
                .totalMonthlyPremiumAmount(request.getTotalMonthlyPremiumAmount())
                .totalCoverageAmount(request.getTotalCoverageAmount())
                .coverages(request.getCoverages())
                .assistances(request.getAssistances())
                .customer(customerDomain)
                .build();
    }

    private Customer toDomain(CustomerRequest req) {
        if (req == null) return null;
        return Customer.builder()
                .documentNumber(req.getDocumentNumber())
                .name(req.getName())
                .type(req.getType())
                .gender(req.getGender())
                .dateOfBirth(parseDate(req.getDateOfBirth()))
                .email(req.getEmail())
                .phoneNumber(req.getPhoneNumber())
                .build();
    }

    public QuotationResponse toResponse(Quotation domain) {
        if (domain == null) {
            return null;
        }

        QuotationResponse resp = new QuotationResponse();
        resp.setId(domain.getId());
        resp.setInsurancePolicyId(domain.getInsurancePolicyId());
        resp.setProductId(domain.getProductId());
        resp.setOfferId(domain.getOfferId());
        resp.setCategory(domain.getCategory());

        resp.setCreatedAt(domain.getCreatedAt() != null ? domain.getCreatedAt().toString() : null);
        resp.setUpdatedAt(domain.getUpdatedAt() != null ? domain.getUpdatedAt().toString() : null);

        resp.setTotalMonthlyPremiumAmount(domain.getTotalMonthlyPremiumAmount());
        resp.setTotalCoverageAmount(domain.getTotalCoverageAmount());
        resp.setCoverages(domain.getCoverages());
        resp.setAssistances(domain.getAssistances());
        resp.setCustomer(toResponse(domain.getCustomer()));
        return resp;
    }

    private CustomerResponse toResponse(Customer domain) {
        if (domain == null) return null;
        CustomerResponse resp = new CustomerResponse();
        resp.setDocumentNumber(domain.getDocumentNumber());
        resp.setName(domain.getName());
        resp.setType(domain.getType());
        resp.setGender(domain.getGender());
        resp.setDateOfBirth(domain.getDateOfBirth() != null ? domain.getDateOfBirth().toString() : null);
        resp.setEmail(domain.getEmail());
        resp.setPhoneNumber(domain.getPhoneNumber());
        return resp;
    }

    private java.time.LocalDate parseDate(String dateStr) {
        if (dateStr == null) return null;
        return java.time.LocalDate.parse(dateStr);
    }
}
