package br.com.company.insurance.adapters.in.rest.mappers;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class QuotationResponse {

    private Long id;

    @JsonProperty("insurance_policy_id")
    private Long insurancePolicyId;

    @JsonProperty("product_id")
    private String productId;

    @JsonProperty("offer_id")
    private String offerId;

    private String category;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("updated_at")
    private String updatedAt;

    @JsonProperty("total_monthly_premium_amount")
    private BigDecimal totalMonthlyPremiumAmount;

    @JsonProperty("total_coverage_amount")
    private BigDecimal totalCoverageAmount;

    private Map<String, BigDecimal> coverages;

    private List<String> assistances;

    private CustomerResponse customer;
}
