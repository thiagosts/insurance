package br.com.company.insurance.application.update;

import br.com.company.insurance.application.SaveQuotationPortOut;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateQuotationPolicyUseCaseImpl implements UpdateQuotationPolicyUseCase {

    private final SaveQuotationPortOut saveQuotationPortOut;

    @Override
    public void updatePolicyNumber(Long quotationId, Long policyNumber) {

        log.info("Updating policy number for Quotation ID: {} with Policy Number: {}", quotationId, policyNumber);

        saveQuotationPortOut.updatePolicyNumber(quotationId, policyNumber);

        log.info("Policy number updated successfully for Quotation ID: {}", quotationId);
    }
}
