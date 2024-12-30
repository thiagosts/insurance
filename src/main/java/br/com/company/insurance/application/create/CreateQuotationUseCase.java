package br.com.company.insurance.application.create;

import br.com.company.insurance.application.CreateQuotationPortIn;
import br.com.company.insurance.application.PublishEventPortOut;
import br.com.company.insurance.application.SaveQuotationPortOut;
import br.com.company.insurance.domain.model.Quotation;
import br.com.company.insurance.domain.services.QuotationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreateQuotationUseCase implements CreateQuotationPortIn {

    private final SaveQuotationPortOut saveQuotationPort;
    private final QuotationService quotationService;
    private final PublishEventPortOut publishEventPort;

    @Override
    public Quotation create(Quotation quotation) {
        log.info("Starting quotation creation for Product ID: {}, Offer ID: {}",
                quotation.getProductId(), quotation.getOfferId());

        quotationService.validateQuotation(quotation);
        log.info("Quotation validated successfully for Product ID: {}, Offer ID: {}",
                quotation.getProductId(), quotation.getOfferId());

        Quotation savedQuotation = saveQuotationPort.save(quotation);

        try {
            publishEventPort.publishQuotationCreated(savedQuotation);
            log.info("Event published successfully for Quotation ID: {}", savedQuotation.getId());
        } catch (Exception e) {
            log.error("Failed to publish event for Quotation ID: {}", savedQuotation.getId(), e);
        }

        return savedQuotation;
    }
}
