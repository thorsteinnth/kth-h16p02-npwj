package se.kth.h16p02.npwj.gretarttsi.hw3.bank.exceptions;

public class RejectedException extends Exception
{
    private static final long serialVersionUID = 4601687973395175716L;

    public RejectedException(String reason)
    {
        super(reason);
    }
}