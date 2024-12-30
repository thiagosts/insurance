package br.com.company.insurance.domain.services;


import br.com.company.insurance.adapters.out.rest.dto.OfferDTO;
import br.com.company.insurance.application.CatalogPortOut;
import br.com.company.insurance.domain.model.Quotation;
import br.com.company.insurance.exceptions.OfferValidationException;
import br.com.company.insurance.exceptions.ProductValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
@RequiredArgsConstructor
@Slf4j
public class QuotationService {

    private final CatalogPortOut catalogPort;

    public void validateQuotation(Quotation quotation) {
        log.info("Validating quotation for Product ID: {}, Offer ID: {}",
                quotation.getProductId(), quotation.getOfferId());

        var product = catalogPort.getProduct(quotation.getProductId());
        if (product.isEmpty() || !product.get().isActive()) {
            String errorMessage = String.format("Product not found or inactive: %s", quotation.getProductId());
            log.error(errorMessage);
            throw new ProductValidationException(errorMessage);
        }

        var offer = catalogPort.getOffer(quotation.getOfferId());
        if (offer.isEmpty() || !offer.get().isActive()) {
            String errorMessage = String.format("Offer not found or inactive: %s", quotation.getOfferId());
            log.error(errorMessage);
            throw new OfferValidationException(errorMessage);
        }

        validateMonthlyPremium(quotation, offer.get());
    }

    private void validateMonthlyPremium(Quotation quotation, OfferDTO offer) {
        BigDecimal premiumAmount = quotation.getTotalMonthlyPremiumAmount();
        BigDecimal minPremium = offer.getMinMonthlyPremium();
        BigDecimal maxPremium = offer.getMaxMonthlyPremium();

        if (premiumAmount.compareTo(minPremium) < 0 || premiumAmount.compareTo(maxPremium) > 0) {
            String errorMessage = String.format(
                    "Monthly premium is out of allowed bounds: %s. Min: %s, Max: %s",
                    premiumAmount.stripTrailingZeros().toPlainString(),
                    minPremium.stripTrailingZeros().toPlainString(),
                    maxPremium.stripTrailingZeros().toPlainString()
            );
            log.error(errorMessage);
            throw new OfferValidationException(errorMessage);
        }
    }
}
