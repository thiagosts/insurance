package br.com.company.insurance.adapters.out.rest;

import br.com.company.insurance.adapters.out.rest.dto.OfferDTO;
import br.com.company.insurance.adapters.out.rest.dto.ProductDTO;
import br.com.company.insurance.application.CatalogPortOut;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class CatalogClientAdapter implements CatalogPortOut {

    private final RestTemplate restTemplate;

    @Value("${catalog.base-url}")
    private String catalogBaseUrl;

    @CircuitBreaker(name = "catalogService", fallbackMethod = "fallbackGetProduct")
    @Retry(name = "catalogService")
    public Optional<ProductDTO> getProduct(String productId) {
        String url = String.format("%s/products/%s", catalogBaseUrl, productId);
        ProductDTO product = restTemplate.getForObject(url, ProductDTO.class);
        log.debug("Successfully retrieved product: {}", product);
        return Optional.ofNullable(product);
    }

    @CircuitBreaker(name = "catalogService", fallbackMethod = "fallbackGetOffer")
    @Retry(name = "catalogService")
    public Optional<OfferDTO> getOffer(String offerId) {
        String url = String.format("%s/offers/%s", catalogBaseUrl, offerId);
        OfferDTO offer = restTemplate.getForObject(url, OfferDTO.class);
        log.debug("Successfully retrieved offer: {}", offer);
        return Optional.ofNullable(offer);
    }

    private Optional<ProductDTO> fallbackGetProduct(String productId, Throwable throwable) {
        log.warn("Fallback triggered for getProduct. Product ID: {}. Reason: {}", productId, throwable.getMessage());
        return Optional.empty();
    }

    private Optional<OfferDTO> fallbackGetOffer(String offerId, Throwable throwable) {
        log.warn("Fallback triggered for getOffer. Offer ID: {}. Reason: {}", offerId, throwable.getMessage());
        return Optional.empty();
    }
}
