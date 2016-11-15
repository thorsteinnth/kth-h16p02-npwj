package com.thorsteinnth.kth_h16p02_npwj.E02.chat;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote
{
    void receiveMsg(String msg) throws RemoteException;

    String getID() throws RemoteException;
}
