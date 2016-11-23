package se.kth.h16p02.npwj.gretarttsi.hw2.shared.RemoteInterfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import se.kth.h16p02.npwj.gretarttsi.hw2.shared.Domain.Item;

public interface MarketPlace extends Remote
{
    void wish(Trader trader, Item item) throws RemoteException;

    void register(Trader trader) throws RemoteException;

    void deregister(Trader trader) throws RemoteException;

    void sell(Trader trader, Item item) throws RemoteException;

    void buy(Trader trader, Item item) throws RemoteException;

    void inspect(Item item) throws RemoteException;
}
