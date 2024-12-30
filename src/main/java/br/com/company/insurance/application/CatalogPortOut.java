package br.com.company.insurance.application;

import br.com.company.insurance.adapters.out.rest.dto.OfferDTO;
import br.com.company.insurance.adapters.out.rest.dto.ProductDTO;

import java.util.Optional;

public interface CatalogPortOut {
    Optional<ProductDTO> getProduct(String productId);

    Optional<OfferDTO> getOffer(String offerId);
}
