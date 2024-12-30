package br.com.company.insurance.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class QuotationNotFoundException extends RuntimeException {
    public QuotationNotFoundException(String message) {
        super(message);
        log.error(message);
    }
}
