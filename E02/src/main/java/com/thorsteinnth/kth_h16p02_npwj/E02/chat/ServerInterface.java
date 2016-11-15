package com.thorsteinnth.kth_h16p02_npwj.E02.chat;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ServerInterface extends Remote {
    void registerClient(ClientInterface obj) throws RemoteException;

    void unregisterClient(ClientInterface obj) throws RemoteException;

    void broadcastMsg(String msg) throws RemoteException;

    List<ClientInterface> getClients() throws RemoteException;
}
