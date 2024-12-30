package br.com.company.insurance.adapters.out.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfferDTO {
    private String id;
    private String product_id;
    private String name;
    private String created_at;
    private boolean active;

    private Map<String, BigDecimal> coverages;
    private List<String> assistances;
    private MonthlyPremiumAmount monthly_premium_amount;

    public BigDecimal getMinMonthlyPremium() {
        if (monthly_premium_amount == null) {
            return BigDecimal.ZERO;
        }
        return monthly_premium_amount.getMin_amount();
    }

    public BigDecimal getMaxMonthlyPremium() {
        if (monthly_premium_amount == null) {
            return BigDecimal.ZERO;
        }
        return monthly_premium_amount.getMax_amount();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MonthlyPremiumAmount {
        private BigDecimal max_amount;
        private BigDecimal min_amount;
        private BigDecimal suggested_amount;
    }
}
