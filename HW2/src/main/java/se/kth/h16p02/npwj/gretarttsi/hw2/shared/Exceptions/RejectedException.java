package se.kth.h16p02.npwj.gretarttsi.hw2.shared.Exceptions;

final public class RejectedException extends Exception {
    private static final long serialVersionUID = -314439670131687936L;

    public RejectedException(String reason) {
        super(reason);
    }
}