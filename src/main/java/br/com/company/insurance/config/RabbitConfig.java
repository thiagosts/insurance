package br.com.company.insurance.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public Queue insuranceQuotaReceivedQueue() {
        return QueueBuilder.durable("insurance_quota_received")
                .withArgument("x-dead-letter-exchange", "quotation.dlx")
                .withArgument("x-dead-letter-routing-key", "insurance.quota.received.dlq")
                .build();
    }

    @Bean
    public Queue insurancePolicyCreatedQueue() {
        return QueueBuilder.durable("insurance_policy_created")
                .withArgument("x-dead-letter-exchange", "quotation.dlx")
                .withArgument("x-dead-letter-routing-key", "insurance.policy.created.dlq")
                .build();
    }

    @Bean
    public Queue insuranceQuotaReceivedDlqQueue() {
        return QueueBuilder.durable("insurance_quota_received.dlq").build();
    }

    @Bean
    public Queue insurancePolicyCreatedDlqQueue() {
        return QueueBuilder.durable("insurance_policy_created.dlq").build();
    }

    @Bean
    public DirectExchange quotationExchange() {
        return new DirectExchange("quotation.exchange", true, false);
    }

    @Bean
    public DirectExchange dlqExchange() {
        return new DirectExchange("quotation.dlx", true, false);
    }

    @Bean
    public Binding insuranceQuotaReceivedBinding(Queue insuranceQuotaReceivedQueue, DirectExchange quotationExchange) {
        return BindingBuilder.bind(insuranceQuotaReceivedQueue).to(quotationExchange).with("insurance.quota.received");
    }

    @Bean
    public Binding insurancePolicyCreatedBinding(Queue insurancePolicyCreatedQueue, DirectExchange quotationExchange) {
        return BindingBuilder.bind(insurancePolicyCreatedQueue).to(quotationExchange).with("insurance.policy.created");
    }

    @Bean
    public Binding insuranceQuotaReceivedDlqBinding(Queue insuranceQuotaReceivedDlqQueue, DirectExchange dlqExchange) {
        return BindingBuilder.bind(insuranceQuotaReceivedDlqQueue).to(dlqExchange).with("insurance.quota.received.dlq");
    }

    @Bean
    public Binding insurancePolicyCreatedDlqBinding(Queue insurancePolicyCreatedDlqQueue, DirectExchange dlqExchange) {
        return BindingBuilder.bind(insurancePolicyCreatedDlqQueue).to(dlqExchange).with("insurance.policy.created.dlq");
    }

    @Bean
    public Queue manualFallbackQueue() {
        return QueueBuilder.durable("manual_fallback_queue").build();
    }

    @Bean
    public DirectExchange manualFallbackExchange() {
        return new DirectExchange("manual_fallback_exchange");
    }

    @Bean
    public Binding manualFallbackBinding(Queue manualFallbackQueue, DirectExchange manualFallbackExchange) {
        return BindingBuilder.bind(manualFallbackQueue).to(manualFallbackExchange).with("manual.fallback");
    }

}
