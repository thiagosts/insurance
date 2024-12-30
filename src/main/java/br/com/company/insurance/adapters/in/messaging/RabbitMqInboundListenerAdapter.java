package br.com.company.insurance.adapters.in.messaging;

import br.com.company.insurance.application.update.UpdateQuotationPolicyUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RabbitMqInboundListenerAdapter {

    private final UpdateQuotationPolicyUseCase updateQuotationPolicyUseCase;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = "insurance_policy_created")
    public void handlePolicyIssued(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        try {
            log.info("Received 'insurance_policy_created' event: {}", message);

            PolicyIssuedMessage policyMessage = objectMapper.readValue(message, PolicyIssuedMessage.class);

            updateQuotationPolicyUseCase.updatePolicyNumber(policyMessage.getQuotationId(), policyMessage.getPolicyNumber());

            channel.basicAck(deliveryTag, false);

            log.info("Processed 'insurance_policy_created' event for Quotation ID: {}", policyMessage.getQuotationId());
        } catch (Exception e) {
            log.error("Failed to process 'insurance_policy_created' event: {}", message, e);

            try {
                channel.basicNack(deliveryTag, false, false);
            } catch (Exception nackException) {
                log.error("Failed to move message to DLQ: {}", message, nackException);
            }
        }
    }
}
