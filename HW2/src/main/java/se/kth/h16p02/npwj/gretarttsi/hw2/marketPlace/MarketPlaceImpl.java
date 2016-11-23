package se.kth.h16p02.npwj.gretarttsi.hw2.marketPlace;

import se.kth.h16p02.npwj.gretarttsi.hw2.shared.Domain.Item;
import se.kth.h16p02.npwj.gretarttsi.hw2.shared.RemoteInterfaces.MarketPlace;
import se.kth.h16p02.npwj.gretarttsi.hw2.shared.RemoteInterfaces.Trader;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class MarketPlaceImpl extends UnicastRemoteObject implements MarketPlace
{
    private MarketPlaceRepository repository;

    public MarketPlaceImpl() throws RemoteException
    {
        super();
        this.repository = new MarketPlaceRepository();
    }

    @Override
    public void addItemToWishlist(Trader trader, Item item, BigDecimal maxPrice) throws RemoteException
    {
        System.out.println("Trader " + trader
                + " adding item to wishlist with max price " + maxPrice
                + ": "+ item
        );
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
    public ArrayList<Item> inspectAvailableItems() throws RemoteException
    {
        System.out.println("Should list available items");
        return new ArrayList<>();
    }

    //region Registration handling

    @Override
    public boolean register(Trader trader) throws RemoteException, TraderAlreadyExistsException
    {
        return this.repository.registerTrader(trader);
    }

    @Override
    public boolean deregister(Trader trader) throws RemoteException
    {
        return this.repository.deregisterTrader(trader);
    }

    @Override
    public boolean checkIfRegistered(Trader trader) throws RemoteException
    {
        return this.repository.isTraderRegistered(trader);
    }

    //endregion
}
