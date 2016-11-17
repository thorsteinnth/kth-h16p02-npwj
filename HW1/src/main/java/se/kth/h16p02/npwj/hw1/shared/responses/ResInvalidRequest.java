package main.java.se.kth.h16p02.npwj.hw1.shared.responses;

public class ResInvalidRequest extends Response
{
    public ResInvalidRequest()
    {
        this.responseType = ResponseType.InvalidRequest;
    }
}
