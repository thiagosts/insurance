package br.com.company.insurance.adapters.out.database;

import br.com.company.insurance.domain.model.Customer;
import br.com.company.insurance.domain.model.Quotation;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class QuotationEntityMapper {

    private final ObjectMapper objectMapper;

    public QuotationEntity toEntity(Quotation domain) {
        if (domain == null) {
            return null;
        }

        return QuotationEntity.builder()
                .id(domain.getId())
                .productId(domain.getProductId())
                .offerId(domain.getOfferId())
                .category(domain.getCategory())
                .totalMonthlyPremiumAmount(domain.getTotalMonthlyPremiumAmount())
                .totalCoverageAmount(domain.getTotalCoverageAmount())
                .coveragesJson(toJson(domain.getCoverages()))
                .assistancesJson(toJson(domain.getAssistances()))
                .customerDocumentNumber(getValueOrNull(domain.getCustomer(), Customer::getDocumentNumber))
                .customerName(getValueOrNull(domain.getCustomer(), Customer::getName))
                .customerType(getValueOrNull(domain.getCustomer(), Customer::getType))
                .customerGender(getValueOrNull(domain.getCustomer(), Customer::getGender))
                .customerDateOfBirth(getValueOrNull(domain.getCustomer(), c -> c.getDateOfBirth() != null ? c.getDateOfBirth().toString() : null))
                .customerEmail(getValueOrNull(domain.getCustomer(), Customer::getEmail))
                .customerPhoneNumber(getValueOrNull(domain.getCustomer(), Customer::getPhoneNumber))
                .insurancePolicyId(domain.getInsurancePolicyId())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }

    public QuotationEntity toEntityWithTimestamps(Quotation quotation) {
        if (quotation == null) {
            return null;
        }

        log.trace("Adding timestamps while mapping domain object to entity: {}", quotation);
        QuotationEntity entity = toEntity(quotation);
        if (entity.getId() == null) {
            entity.setCreatedAt(LocalDateTime.now());
        }
        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }

    public Quotation toDomain(QuotationEntity entity) {
        if (entity == null) {
            return null;
        }

        return Quotation.builder()
                .id(entity.getId())
                .productId(entity.getProductId())
                .offerId(entity.getOfferId())
                .category(entity.getCategory())
                .totalMonthlyPremiumAmount(entity.getTotalMonthlyPremiumAmount())
                .totalCoverageAmount(entity.getTotalCoverageAmount())
                .coverages(fromJson(entity.getCoveragesJson(), new TypeReference<Map<String, BigDecimal>>() {}))
                .assistances(fromJson(entity.getAssistancesJson(), new TypeReference<List<String>>() {}))
                .customer(Customer.builder()
                        .documentNumber(entity.getCustomerDocumentNumber())
                        .name(entity.getCustomerName())
                        .type(entity.getCustomerType())
                        .gender(entity.getCustomerGender())
                        .dateOfBirth(parseDate(entity.getCustomerDateOfBirth()))
                        .email(entity.getCustomerEmail())
                        .phoneNumber(entity.getCustomerPhoneNumber())
                        .build())
                .insurancePolicyId(entity.getInsurancePolicyId())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    private String toJson(Object obj) {
        try {
            return obj != null ? objectMapper.writeValueAsString(obj) : null;
        } catch (Exception e) {
            log.error("Failed to convert object to JSON: {}", obj, e);
            return null;
        }
    }

    private <T> T fromJson(String json, TypeReference<T> typeReference) {
        try {
            return json != null ? objectMapper.readValue(json, typeReference) : null;
        } catch (Exception e) {
            log.error("Failed to convert JSON to object: {}", json, e);
            return null;
        }
    }

    private LocalDate parseDate(String dateStr) {
        try {
            return dateStr != null ? LocalDate.parse(dateStr) : null;
        } catch (Exception e) {
            log.error("Failed to parse date string: {}", dateStr, e);
            return null;
        }
    }

    private <T, R> R getValueOrNull(T obj, java.util.function.Function<T, R> mapper) {
        return obj != null ? mapper.apply(obj) : null;
    }
}
