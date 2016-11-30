package se.kth.h16p02.npwj.gretarttsi.hw3.bank.server.integration;

final public class BankDBException extends Exception {
    private static final long serialVersionUID = -41424240839829672L;

    public BankDBException(String reason) {
        super(reason);
    }

    public BankDBException(String reason, Throwable rootCause) {
        super(reason, rootCause);
    }
}
