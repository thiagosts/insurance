package br.com.company.insurance.adapters.out.messaging;

import br.com.company.insurance.domain.model.Quotation;
import br.com.company.insurance.exceptions.PublishEventException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

class RabbitMqPublisherAdapterTest {

    private final RabbitTemplate rabbitTemplate = Mockito.mock(RabbitTemplate.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RabbitMqPublisherAdapter publisher = new RabbitMqPublisherAdapter(rabbitTemplate, objectMapper);

    @Test
    void shouldPublishQuotationCreatedEvent() throws Exception {
        Quotation quotation = Quotation.builder()
                .id(123L)
                .productId("1b2da7cc-b367-4196-8a78-9cfeec21f587")
                .offerId("bdc56d77-348c-4bf0-908f-22d402ee715c")
                .build();

        publisher.publishQuotationCreated(quotation);

        Mockito.verify(rabbitTemplate).convertAndSend(
                eq("quotation.exchange"),
                eq("quotation.created"),
                anyString()
        );
    }

    @Test
    void shouldValidateGeneratedMessage() throws Exception {
        Quotation quotation = Quotation.builder()
                .id(123L)
                .productId("1b2da7cc-b367-4196-8a78-9cfeec21f587")
                .offerId("bdc56d77-348c-4bf0-908f-22d402ee715c")
                .build();

        String generatedMessage = objectMapper.writeValueAsString(quotation);

        String expectedMessage = "{\"id\":123,\"productId\":\"1b2da7cc-b367-4196-8a78-9cfeec21f587\",\"offerId\":\"bdc56d77-348c-4bf0-908f-22d402ee715c\"}";

        ObjectNode generatedNode = (ObjectNode) objectMapper.readTree(generatedMessage);
        ObjectNode expectedNode = (ObjectNode) objectMapper.readTree(expectedMessage);

        generatedNode.remove(Arrays.asList("category", "totalMonthlyPremiumAmount", "totalCoverageAmount", "coverages", "assistances", "customer", "insurancePolicyId", "createdAt", "updatedAt"));

        assertEquals(expectedNode.toString(), generatedNode.toString());
    }

    @Test
    void shouldHandlePublishFailure() {
        Quotation quotation = Quotation.builder()
                .id(123L)
                .productId("1b2da7cc-b367-4196-8a78-9cfeec21f587")
                .offerId("bdc56d77-348c-4bf0-908f-22d402ee715c")
                .build();

        Mockito.doThrow(new RuntimeException("Connection error"))
                .when(rabbitTemplate).convertAndSend(anyString(), anyString(), anyString());

        PublishEventException exception = assertThrows(PublishEventException.class,
                () -> publisher.publishQuotationCreated(quotation));

        assertTrue(exception.getMessage().contains("Failed to publish 'quotation.created' event"));
    }

    @Test
    void shouldHandleSerializationError() throws Exception {
        Quotation quotation = Quotation.builder()
                .id(123L)
                .build();

        ObjectMapper faultyObjectMapper = Mockito.mock(ObjectMapper.class);
        RabbitMqPublisherAdapter faultyPublisher = new RabbitMqPublisherAdapter(rabbitTemplate, faultyObjectMapper);

        Mockito.when(faultyObjectMapper.writeValueAsString(quotation))
                .thenThrow(new JsonProcessingException("Serialization error") {
                });

        PublishEventException exception = assertThrows(PublishEventException.class,
                () -> faultyPublisher.publishQuotationCreated(quotation));

        assertTrue(exception.getMessage().contains("Failed to publish 'quotation.created' event"));
    }

    @Test
    void shouldPublishWithMissingFields() throws Exception {
        Quotation quotation = Quotation.builder()
                .id(null)
                .productId(null)
                .offerId(null)
                .build();

        publisher.publishQuotationCreated(quotation);

        Mockito.verify(rabbitTemplate).convertAndSend(anyString(), anyString(), anyString());
    }
}