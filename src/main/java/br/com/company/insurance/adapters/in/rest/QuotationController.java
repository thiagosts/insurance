package br.com.company.insurance.adapters.in.rest;

import br.com.company.insurance.adapters.in.rest.mappers.QuotationMapper;
import br.com.company.insurance.adapters.in.rest.mappers.QuotationRequest;
import br.com.company.insurance.adapters.in.rest.mappers.QuotationResponse;
import br.com.company.insurance.application.CreateQuotationPortIn;
import br.com.company.insurance.application.FindQuotationPortOut;
import br.com.company.insurance.domain.model.Quotation;
import br.com.company.insurance.exceptions.QuotationNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/quotations")
@RequiredArgsConstructor
public class QuotationController {

    private final CreateQuotationPortIn createQuotationPortIn;
    private final FindQuotationPortOut findQuotationPortOut;
    private final QuotationMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public QuotationResponse create(@Valid @RequestBody final QuotationRequest request) {
        Quotation quotation = mapper.toDomain(request);
        Quotation created = createQuotationPortIn.create(quotation);
        return mapper.toResponse(created);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public QuotationResponse findById(@PathVariable final Long id) {
        Quotation found = findQuotationPortOut.findById(id);
        if (found == null) {
            throw new QuotationNotFoundException("Cotação com ID " + id + " não encontrada.");
        }
        return mapper.toResponse(found);
    }
}
