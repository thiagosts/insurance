package br.com.company.insurance.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Quotation {

    private Long id;

    private String productId;
    private String offerId;

    private String category;

    private BigDecimal totalMonthlyPremiumAmount;
    private BigDecimal totalCoverageAmount;

    private Map<String, BigDecimal> coverages;

    private List<String> assistances;

    private Customer customer;

    private Long insurancePolicyId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
