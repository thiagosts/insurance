package br.com.company.insurance.adapters.out.messaging;

import br.com.company.insurance.application.PublishEventPortOut;
import br.com.company.insurance.domain.model.Quotation;
import br.com.company.insurance.exceptions.PublishEventException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RabbitMqPublisherAdapter implements PublishEventPortOut {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void publishQuotationCreated(Quotation quotation) {
        try {
            String message = objectMapper.writeValueAsString(quotation);
            rabbitTemplate.convertAndSend("quotation.exchange", "quotation.created", message);
            log.info("Published 'quotation.created' event for Quotation ID: {} with message: {}",
                    quotation.getId(), message);
        } catch (Exception e) {
            log.error("Failed to publish 'quotation.created' event for Quotation ID: {}", quotation.getId(), e);
            sendToFallbackQueue(quotation, e);
            throw new PublishEventException("Failed to publish 'quotation.created' event for Quotation ID: " + quotation.getId(), e);
        }
    }

    private void sendToFallbackQueue(Quotation quotation, Exception originalException) {
        try {
            String fallbackMessage = objectMapper.writeValueAsString(quotation);
            rabbitTemplate.convertAndSend("manual_fallback_exchange", "manual.fallback", fallbackMessage);
            log.warn("Message sent to manual fallback queue for Quotation ID: {}", quotation.getId());
        } catch (Exception fallbackException) {
            log.error("Failed to send message to manual fallback queue for Quotation ID: {}",
                    quotation.getId(), fallbackException);
            throw new PublishEventException(
                    "Failed to publish 'quotation.created' event and fallback for Quotation ID: " + quotation.getId(),
                    originalException);
        }
    }
}
