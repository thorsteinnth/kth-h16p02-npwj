package se.kth.h16p02.npwj.gretarttsi.hw3.bank.exceptions;

public class InsufficientFundsException extends Exception
{
    public InsufficientFundsException(String message)
    {
        super(message);
    }
}
