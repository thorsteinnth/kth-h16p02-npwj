package se.kth.h16p02.npwj.gretarttsi.hw2.shared.RemoteInterfaces;

import java.math.BigDecimal;
import java.rmi.Remote;
import java.rmi.RemoteException;
import se.kth.h16p02.npwj.gretarttsi.hw2.shared.Domain.Item;

public interface MarketPlace extends Remote
{
    /**
     * Add item to wishlist
     * */
    void addItemToWishlist(Trader trader, Item item, BigDecimal maxPrice) throws RemoteException;

    /**
     * Register client (trader)
     * */
    void register(Trader trader) throws RemoteException;

    /**
     * Deregister client (trader)
     * */
    void deregister(Trader trader) throws RemoteException;

    /**
     * Check if a trader is registered in the market place
     * */
    boolean checkIfRegistered(Trader trader) throws RemoteException;

    /**
     * Put item up for sale
     * */
    void sell(Trader trader, Item item) throws RemoteException;

    /**
     * Buy item
     * */
    void buy(Trader trader, Item item) throws RemoteException;

    /**
     * Inspect what items are available on the marketplace
     * */
    void inspectAvailableItems() throws RemoteException;
}
