package br.com.company.insurance.adapters.out.messaging;

import br.com.company.insurance.domain.model.Quotation;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@Testcontainers
class RabbitMqPublisherAdapterIntegrationTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Container
    static RabbitMQContainer rabbitMQContainer = new RabbitMQContainer("rabbitmq:3.12-management")
            .withExposedPorts(5672, 15672)
            .withExchange("quotation.exchange", "direct")
            .withQueue("insurance_quota_received")
            .withQueue("insurance_policy_created")
            .withBinding("quotation.exchange", "insurance_quota_received", "insurance.quota.received")
            .withBinding("quotation.exchange", "insurance_policy_created", "insurance.policy.created");

    @Test
    void shouldPublishQuotationCreatedEventSuccessfully() {
        RabbitMqPublisherAdapter publisher = new RabbitMqPublisherAdapter(rabbitTemplate, objectMapper);
        Quotation quotation = Quotation.builder()
                .id(123L)
                .productId("1b2da7cc-b367-4196-8a78-9cfeec21f587")
                .offerId("bdc56d77-348c-4bf0-908f-22d402ee715c")
                .build();
        assertDoesNotThrow(() -> publisher.publishQuotationCreated(quotation));
    }
}
