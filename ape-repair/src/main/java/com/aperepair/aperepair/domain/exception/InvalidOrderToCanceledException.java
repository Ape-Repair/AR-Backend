package com.aperepair.aperepair.domain.exception;

public class InvalidOrderToCanceledException extends Exception {

    public InvalidOrderToCanceledException(String message) {
        super(message);
    }
}
