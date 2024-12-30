package br.com.company.insurance.adapters.out.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataQuotationRepository extends JpaRepository<QuotationEntity, Long> {
}
