package se.kth.h16p02.npwj.gretarttsi.hw2.shared.RemoteInterfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Trader extends Remote
{
    String getUsername() throws RemoteException;
    void wishIsAvailable (String item) throws RemoteException;
    void itemSold(String item) throws RemoteException;
}
