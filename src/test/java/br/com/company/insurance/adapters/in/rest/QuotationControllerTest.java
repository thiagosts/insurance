package br.com.company.insurance.adapters.in.rest;

import br.com.company.insurance.adapters.in.rest.mappers.QuotationMapper;
import br.com.company.insurance.adapters.in.rest.mappers.QuotationRequest;
import br.com.company.insurance.adapters.in.rest.mappers.QuotationResponse;
import br.com.company.insurance.application.CreateQuotationPortIn;
import br.com.company.insurance.application.FindQuotationPortOut;
import br.com.company.insurance.domain.model.Quotation;
import br.com.company.insurance.exceptions.QuotationNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuotationControllerTest {

    @Mock
    private CreateQuotationPortIn createQuotationPortIn;

    @Mock
    private FindQuotationPortOut findQuotationPortOut;

    @Mock
    private QuotationMapper mapper;

    @InjectMocks
    private QuotationController controller;

    @Test
    void shouldCreateQuotationSuccessfully() {
        QuotationRequest request = new QuotationRequest();
        Quotation domain = new Quotation();
        Quotation created = new Quotation();
        QuotationResponse response = new QuotationResponse();

        when(mapper.toDomain(request)).thenReturn(domain);
        when(createQuotationPortIn.create(domain)).thenReturn(created);
        when(mapper.toResponse(created)).thenReturn(response);

        QuotationResponse result = controller.create(request);

        assertNotNull(result);
        assertEquals(response, result);

        verify(mapper).toDomain(request);
        verify(createQuotationPortIn).create(domain);
        verify(mapper).toResponse(created);
    }

    @Test
    void shouldFindQuotationByIdSuccessfully() {
        Long id = 1L;
        Quotation found = new Quotation();
        QuotationResponse response = new QuotationResponse();

        when(findQuotationPortOut.findById(id)).thenReturn(found);
        when(mapper.toResponse(found)).thenReturn(response);

        QuotationResponse result = controller.findById(id);

        assertNotNull(result);
        assertEquals(response, result);

        verify(findQuotationPortOut).findById(id);
        verify(mapper).toResponse(found);
    }

    @Test
    void shouldThrowExceptionWhenQuotationNotFound() {
        Long id = 1L;

        when(findQuotationPortOut.findById(id)).thenReturn(null);

        QuotationNotFoundException exception = assertThrows(
                QuotationNotFoundException.class,
                () -> controller.findById(id)
        );

        assertEquals("Cotação com ID " + id + " não encontrada.", exception.getMessage());

        verify(findQuotationPortOut).findById(id);
        verifyNoInteractions(mapper);
    }
}