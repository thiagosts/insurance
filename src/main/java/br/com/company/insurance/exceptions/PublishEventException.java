package br.com.company.insurance.exceptions;

public class PublishEventException extends RuntimeException {
    public PublishEventException(String message, Throwable cause) {
        super(message, cause);
    }
}
