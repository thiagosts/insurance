package br.com.company.insurance.domain.services;

import br.com.company.insurance.adapters.out.rest.dto.OfferDTO;
import br.com.company.insurance.adapters.out.rest.dto.ProductDTO;
import br.com.company.insurance.application.CatalogPortOut;
import br.com.company.insurance.domain.model.Quotation;
import br.com.company.insurance.exceptions.OfferValidationException;
import br.com.company.insurance.exceptions.ProductValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class QuotationServiceTest {

    @InjectMocks
    private QuotationService quotationService;

    @Mock
    private CatalogPortOut catalogPort;

    @Test
    void shouldThrowProductValidationExceptionWhenProductIsNotFound() {
        Quotation quotation = Quotation.builder()
                .productId("invalid-product-id")
                .offerId("valid-offer-id")
                .build();

        Mockito.when(catalogPort.getProduct("invalid-product-id")).thenReturn(Optional.empty());

        ProductValidationException exception = Assertions.assertThrows(ProductValidationException.class,
                () -> quotationService.validateQuotation(quotation));

        Assertions.assertEquals("Product not found or inactive: invalid-product-id", exception.getMessage());
    }

    @Test
    void shouldThrowOfferValidationExceptionWhenOfferIsNotFound() {
        Quotation quotation = Quotation.builder()
                .productId("valid-product-id")
                .offerId("invalid-offer-id")
                .build();

        ProductDTO product = new ProductDTO(
                "valid-product-id",
                "Valid Product",
                "2024-12-29T00:00:00",
                true,
                List.of("offer-id-1")
        );

        Mockito.when(catalogPort.getProduct("valid-product-id")).thenReturn(Optional.of(product));
        Mockito.when(catalogPort.getOffer("invalid-offer-id")).thenReturn(Optional.empty());

        OfferValidationException exception = Assertions.assertThrows(OfferValidationException.class,
                () -> quotationService.validateQuotation(quotation));

        Assertions.assertEquals("Offer not found or inactive: invalid-offer-id", exception.getMessage());
    }

    @Test
    void shouldThrowOfferValidationExceptionWhenPremiumIsOutOfBounds() {
        Quotation quotation = Quotation.builder()
                .productId("valid-product-id")
                .offerId("valid-offer-id")
                .totalMonthlyPremiumAmount(BigDecimal.valueOf(150.00))
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

        OfferValidationException exception = Assertions.assertThrows(OfferValidationException.class,
                () -> quotationService.validateQuotation(quotation));

        Assertions.assertEquals(
                "Monthly premium is out of allowed bounds: 150. Min: 50, Max: 100",
                exception.getMessage()
        );
    }
    @Test
    void shouldPassValidationSuccessfully() {
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
}
