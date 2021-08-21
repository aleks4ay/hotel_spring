package org.aleks4ay.hotel.exception;

public class NoMoneyException extends RuntimeException {
    public NoMoneyException(String message) {
        super(message);
    }
}