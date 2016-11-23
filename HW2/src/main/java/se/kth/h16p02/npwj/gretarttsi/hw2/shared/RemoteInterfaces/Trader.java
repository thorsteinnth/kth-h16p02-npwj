package se.kth.h16p02.npwj.gretarttsi.hw2.shared.RemoteInterfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

import se.kth.h16p02.npwj.gretarttsi.hw2.shared.Domain.Item;

public interface Trader extends Remote {

    void wishIsAvailable (String item) throws RemoteException;

    void itemSold(String item) throws RemoteException;

}
