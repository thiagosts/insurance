package br.com.company.insurance.domain.services;

import br.com.company.insurance.adapters.out.rest.dto.OfferDTO;
import br.com.company.insurance.adapters.out.rest.dto.ProductDTO;
import br.com.company.insurance.application.CatalogPortOut;
import br.com.company.insurance.domain.model.Quotation;
import br.com.company.insurance.exceptions.ProductValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@Testcontainers
class QuotationServiceIntegrationTest {

    @Autowired
    private QuotationService quotationService;

    @Autowired
    private CatalogPortOut catalogPort;

    @TestConfiguration
    static class MockConfiguration {

        @Bean
        public CatalogPortOut catalogPort() {
            return Mockito.mock(CatalogPortOut.class);
        }
    }

    @Test
    void shouldValidateQuotationSuccessfully() {
        Quotation quotation = Quotation.builder()
                .productId("valid-product-id")
                .offerId("valid-offer-id")
                .totalMonthlyPremiumAmount(BigDecimal.valueOf(75.00))
                .build();

        OfferDTO.MonthlyPremiumAmount monthlyPremium = new OfferDTO.MonthlyPremiumAmount(
                BigDecimal.valueOf(100.00),
                BigDecimal.valueOf(50.00),
                BigDecimal.valueOf(75.00)
        );

        OfferDTO offer = new OfferDTO(
                "valid-offer-id",
                "valid-product-id",
                "Offer Name",
                "2024-12-29T00:00:00",
                true,
                null,
                null,
                monthlyPremium
        );

        ProductDTO product = new ProductDTO(
                "valid-product-id",
                "Valid Product",
                "2024-12-29T00:00:00",
                true,
                List.of("valid-offer-id")
        );

        Mockito.when(catalogPort.getProduct("valid-product-id")).thenReturn(Optional.of(product));
        Mockito.when(catalogPort.getOffer("valid-offer-id")).thenReturn(Optional.of(offer));

        Assertions.assertDoesNotThrow(() -> quotationService.validateQuotation(quotation));
    }

    @Test
    void shouldThrowExceptionWhenProductIsInactive() {
        Quotation quotation = Quotation.builder()
                .productId("inactive-product-id")
                .offerId("valid-offer-id")
                .build();

        ProductDTO product = new ProductDTO(
                "inactive-product-id",
                "Inactive Product",
                "2024-12-29T00:00:00",
                false,
                List.of()
        );

        Mockito.when(catalogPort.getProduct("inactive-product-id")).thenReturn(Optional.of(product));

        ProductValidationException exception = Assertions.assertThrows(ProductValidationException.class,
                () -> quotationService.validateQuotation(quotation));

        Assertions.assertEquals("Product not found or inactive: inactive-product-id", exception.getMessage());
    }
}
