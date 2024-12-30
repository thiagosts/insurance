package br.com.company.insurance.adapters.out.rest;

import br.com.company.insurance.adapters.out.rest.dto.OfferDTO;
import br.com.company.insurance.adapters.out.rest.dto.ProductDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CatalogClientAdapterTest {

    private static final String BASE_URL = "http://localhost:8081";

    @Mock
    private RestTemplate restTemplate;

    private CatalogClientAdapter catalogClientAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        catalogClientAdapter = new CatalogClientAdapter(restTemplate);
        ReflectionTestUtils.setField(catalogClientAdapter, "catalogBaseUrl", BASE_URL);
    }

    @Test
    void shouldReturnProductSuccessfully() {
        String productId = "valid-product-id";
        String url = BASE_URL + "/products/" + productId;

        ProductDTO product = createProductDTO(productId, true);

        when(restTemplate.getForObject(url, ProductDTO.class)).thenReturn(product);

        Optional<ProductDTO> result = catalogClientAdapter.getProduct(productId);

        assertTrue(result.isPresent());
        assertEquals(productId, result.get().getId());
        assertTrue(result.get().isActive());

        verify(restTemplate).getForObject(url, ProductDTO.class);
    }

    @Test
    void shouldReturnOfferSuccessfully() {
        String offerId = "valid-offer-id";
        String url = BASE_URL + "/offers/" + offerId;

        OfferDTO offer = createOfferDTO(offerId, true, BigDecimal.valueOf(50.00), BigDecimal.valueOf(100.00));

        when(restTemplate.getForObject(url, OfferDTO.class)).thenReturn(offer);

        Optional<OfferDTO> result = catalogClientAdapter.getOffer(offerId);

        assertTrue(result.isPresent());
        assertEquals(offerId, result.get().getId());
        assertTrue(result.get().isActive());
        assertNotNull(result.get().getMonthly_premium_amount());
        assertEquals(BigDecimal.valueOf(50.00), result.get().getMinMonthlyPremium());
        assertEquals(BigDecimal.valueOf(100.00), result.get().getMaxMonthlyPremium());

        verify(restTemplate).getForObject(url, OfferDTO.class);
    }

    @Test
    void shouldHandleGetProductFailureAndReturnEmpty() {
        String invalidProductId = "invalid-product-id";
        String url = BASE_URL + "/products/" + invalidProductId;

        Optional<ProductDTO> result = catalogClientAdapter.getProduct(invalidProductId);

        assertFalse(result.isPresent(), "Expected an empty Optional due to simulated error.");

        verify(restTemplate).getForObject(url, ProductDTO.class);
    }

    @Test
    void shouldHandleGetOfferFailureAndReturnEmpty() {
        String invalidOfferId = "invalid-offer-id";
        String url = BASE_URL + "/offers/" + invalidOfferId;

        Optional<OfferDTO> result = catalogClientAdapter.getOffer(invalidOfferId);

        assertFalse(result.isPresent(), "Expected an empty Optional due to simulated error.");

        verify(restTemplate).getForObject(url, OfferDTO.class);
    }

    private ProductDTO createProductDTO(String id, boolean isActive) {
        ProductDTO product = new ProductDTO();
        product.setId(id);
        product.setActive(isActive);
        return product;
    }

    private OfferDTO createOfferDTO(String id, boolean isActive, BigDecimal minAmount, BigDecimal maxAmount) {
        OfferDTO.MonthlyPremiumAmount monthlyPremiumAmount = new OfferDTO.MonthlyPremiumAmount();
        monthlyPremiumAmount.setMin_amount(minAmount);
        monthlyPremiumAmount.setMax_amount(maxAmount);

        OfferDTO offer = new OfferDTO();
        offer.setId(id);
        offer.setActive(isActive);
        offer.setMonthly_premium_amount(monthlyPremiumAmount);
        return offer;
    }
}
