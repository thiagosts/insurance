package br.com.company.insurance.adapters.out.database;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "quotations")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuotationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id", nullable = false)
    @NotBlank(message = "Product ID cannot be blank.")
    private String productId;

    @Column(name = "offer_id", nullable = false)
    @NotBlank(message = "Offer ID cannot be blank.")
    private String offerId;

    @Column(nullable = false)
    @NotBlank(message = "Category cannot be blank.")
    private String category;

    @Column(name = "total_monthly_premium_amount", nullable = false, precision = 15, scale = 2)
    @NotNull(message = "Total monthly premium amount cannot be null.")
    @DecimalMin(value = "0.01", message = "Total monthly premium amount must be greater than zero.")
    private BigDecimal totalMonthlyPremiumAmount;

    @Column(name = "total_coverage_amount", nullable = false, precision = 15, scale = 2)
    @NotNull(message = "Total coverage amount cannot be null.")
    @DecimalMin(value = "0.01", message = "Total coverage amount must be greater than zero.")
    private BigDecimal totalCoverageAmount;

    @Column(name = "coverages_json", columnDefinition = "TEXT")
    private String coveragesJson;

    @Column(name = "assistances_json", columnDefinition = "TEXT")
    private String assistancesJson;

    @Column(name = "customer_document_number", length = 50)
    @Pattern(regexp = "\\d{11}", message = "Customer document number must have 11 digits.")
    private String customerDocumentNumber;

    @Column(name = "customer_name", length = 255)
    @NotBlank(message = "Customer name cannot be blank.")
    private String customerName;

    @Column(name = "customer_type", length = 50)
    @NotBlank(message = "Customer type cannot be blank.")
    private String customerType;

    @Column(name = "customer_gender", length = 10)
    @Pattern(regexp = "MALE|FEMALE|OTHER", message = "Customer gender must be MALE, FEMALE, or OTHER.")
    private String customerGender;

    @Column(name = "customer_date_of_birth", length = 10)
    private String customerDateOfBirth;

    @Column(name = "customer_email", length = 255)
    @Email(message = "Customer email must be valid.")
    private String customerEmail;

    @Column(name = "customer_phone_number", length = 50)
    @Pattern(regexp = "\\d{10,15}", message = "Customer phone number must have between 10 and 15 digits.")
    private String customerPhoneNumber;

    @Column(name = "insurance_policy_id")
    private Long insurancePolicyId;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}