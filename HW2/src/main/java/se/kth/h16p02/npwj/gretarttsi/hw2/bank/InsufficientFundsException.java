package se.kth.h16p02.npwj.gretarttsi.hw2.bank;

public class InsufficientFundsException extends Exception
{
    public InsufficientFundsException(String message)
    {
        super(message);
    }
}
