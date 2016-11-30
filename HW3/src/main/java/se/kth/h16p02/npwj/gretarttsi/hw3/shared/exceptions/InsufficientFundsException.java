package se.kth.h16p02.npwj.gretarttsi.hw3.shared.exceptions;

import java.security.cert.Extension;

public class InsufficientFundsException extends Exception {
    public InsufficientFundsException(String message) {
        super(message);
    }
}

