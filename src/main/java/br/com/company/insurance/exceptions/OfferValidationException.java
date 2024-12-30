package br.com.company.insurance.exceptions;

public class OfferValidationException extends RuntimeException {
    public OfferValidationException(String message) {
        super(message);
    }
}
