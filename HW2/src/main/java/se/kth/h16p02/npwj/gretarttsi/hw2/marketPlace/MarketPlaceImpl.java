package se.kth.h16p02.npwj.gretarttsi.hw2.marketPlace;


import se.kth.h16p02.npwj.gretarttsi.hw2.shared.Domain.Item;
import se.kth.h16p02.npwj.gretarttsi.hw2.shared.RemoteInterfaces.MarketPlace;
import se.kth.h16p02.npwj.gretarttsi.hw2.shared.RemoteInterfaces.Trader;
import sun.jvm.hotspot.oops.Mark;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by GretarAtli on 23/11/2016.
 */
public class MarketPlaceImpl extends UnicastRemoteObject implements MarketPlace {

    MarketPlaceImpl() throws RemoteException{
        super();
    }

    @Override
    public void wish(Trader trader, Item item) throws RemoteException {
        System.out.println("Client making a wish");
    }

    @Override
    public void register(Trader trader) throws RemoteException {
        System.out.println("Client making a wish");
    }

    @Override
    public void unregister(Trader trader) throws RemoteException {
        System.out.println("Client making a wish");
    }

    @Override
    public void sell(Trader trader, Item item) throws RemoteException {
        System.out.println("Client making a wish");
    }

    @Override
    public void buy(Trader trader, Item item) throws RemoteException {
        System.out.println("Client making a wish");
    }

    @Override
    public void inspect(Item item) throws RemoteException {

    }
}
