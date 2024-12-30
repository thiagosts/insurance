package br.com.company.insurance.adapters.out.database;

import br.com.company.insurance.application.FindQuotationPortOut;
import br.com.company.insurance.application.SaveQuotationPortOut;
import br.com.company.insurance.domain.model.Quotation;
import br.com.company.insurance.exceptions.QuotationNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class QuotationRepositoryAdapter implements FindQuotationPortOut, SaveQuotationPortOut {

    private final SpringDataQuotationRepository jpaRepository;
    private final QuotationEntityMapper mapper;

    @Override
    public Quotation save(Quotation quotation) {
        log.debug("Saving quotation with data: {}", quotation);
        var entity = mapper.toEntityWithTimestamps(quotation);
        var savedEntity = jpaRepository.save(entity);
        log.info("Quotation saved successfully with ID: {}", savedEntity.getId());
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Quotation findById(Long id) {
        log.debug("Fetching quotation with ID: {}", id);
        return jpaRepository.findById(id)
                .map(mapper::toDomain)
                .orElseThrow(() -> new QuotationNotFoundException(
                        String.format("Quotation with ID %s not found.", id)
                ));
    }

    @Override
    public void updatePolicyNumber(Long quotationId, Long policyNumber) {
        log.debug("Updating policy number for quotation ID: {} with policy number: {}", quotationId, policyNumber);
        var entity = jpaRepository.findById(quotationId)
                .orElseThrow(() -> new QuotationNotFoundException(
                        String.format("Quotation with ID %s not found while updating policy number.", quotationId)
                ));

        entity.setInsurancePolicyId(policyNumber);
        entity.setUpdatedAt(LocalDateTime.now());
        jpaRepository.save(entity);

        log.info("Policy number updated successfully for quotation ID: {}", quotationId);
    }
}
