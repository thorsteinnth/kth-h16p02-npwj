package se.kth.h16p02.npwj.gretarttsi.hw3.shared.remoteInterfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Trader extends Remote
{
    String getUsername() throws RemoteException;
    void wishListAvailableNotification (String notification) throws RemoteException;
    void itemSoldNotification(String notification) throws RemoteException;
}
