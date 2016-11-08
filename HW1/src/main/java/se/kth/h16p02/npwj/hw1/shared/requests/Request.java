package main.java.se.kth.h16p02.npwj.hw1.shared.requests;

public abstract class Request
{
    protected RequestType requestType;

    public RequestType getRequestType()
    {
        return requestType;
    }
}
