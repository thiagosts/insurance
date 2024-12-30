package br.com.company.insurance.adapters.out.database;

import br.com.company.insurance.domain.model.Quotation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;


@ExtendWith(MockitoExtension.class)
class QuotationRepositoryAdapterTest {

    @Mock
    private SpringDataQuotationRepository jpaRepository;

    @Mock
    private QuotationEntityMapper mapper;

    @InjectMocks
    private QuotationRepositoryAdapter adapter;

    @Test
    void shouldSaveQuotationSuccessfully() {
        Quotation quotation = Quotation.builder().id(null).build();
        QuotationEntity entity = new QuotationEntity();
        QuotationEntity savedEntity = new QuotationEntity();
        savedEntity.setId(123L);

        Mockito.when(mapper.toEntityWithTimestamps(quotation)).thenReturn(entity);
        Mockito.when(jpaRepository.save(entity)).thenReturn(savedEntity);
        Mockito.when(mapper.toDomain(savedEntity)).thenReturn(quotation);

        Quotation result = adapter.save(quotation);

        Assertions.assertNotNull(result);
        Mockito.verify(jpaRepository).save(entity);
        Mockito.verify(mapper).toEntityWithTimestamps(quotation);
    }

    @Test
    void shouldFindQuotationById() {
        QuotationEntity entity = new QuotationEntity();
        entity.setId(123L);
        Quotation quotation = Quotation.builder().id(123L).build();

        Mockito.when(jpaRepository.findById(123L)).thenReturn(Optional.of(entity));
        Mockito.when(mapper.toDomain(entity)).thenReturn(quotation);

        Quotation result = adapter.findById(123L);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(123L, result.getId());
        Mockito.verify(jpaRepository).findById(123L);
        Mockito.verify(mapper).toDomain(entity);
    }
}