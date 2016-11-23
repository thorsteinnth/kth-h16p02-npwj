package se.kth.h16p02.npwj.gretarttsi.hw2.marketPlace;

import se.kth.h16p02.npwj.gretarttsi.hw2.shared.Domain.Item;
import se.kth.h16p02.npwj.gretarttsi.hw2.shared.RemoteInterfaces.MarketPlace;
import se.kth.h16p02.npwj.gretarttsi.hw2.shared.RemoteInterfaces.Trader;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class MarketPlaceImpl extends UnicastRemoteObject implements MarketPlace
{
    public MarketPlaceImpl() throws RemoteException
    {
        super();
    }

    @Override
    public void wish(Trader trader, Item item) throws RemoteException
    {
        System.out.println("Trader " + trader + " making a wish for item " + item);
    }

    @Override
    public void register(Trader trader) throws RemoteException
    {
        System.out.println("Trader registering");
    }

    @Override
    public void deregister(Trader trader) throws RemoteException
    {
        System.out.println("Trader deregistering");
    }

    @Override
    public void sell(Trader trader, Item item) throws RemoteException
    {
        System.out.println("Trader " + trader + " selling: " + item);
    }

    @Override
    public void buy(Trader trader, Item item) throws RemoteException
    {
        System.out.println("Trader " + trader + " buying: " + item);
    }

    @Override
    public void inspect(Item item) throws RemoteException
    {
        System.out.println("Should inspect item: " + item);
    }
}
