package se.kth.h16p02.npwj.gretarttsi.hw2.shared.RemoteInterfaces;

import java.math.BigDecimal;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import se.kth.h16p02.npwj.gretarttsi.hw2.marketplace.ItemAlreadyExistsException;
import se.kth.h16p02.npwj.gretarttsi.hw2.marketplace.ItemNotFoundException;
import se.kth.h16p02.npwj.gretarttsi.hw2.marketplace.TraderAlreadyExistsException;
import se.kth.h16p02.npwj.gretarttsi.hw2.marketplace.TraderNotFoundException;
import se.kth.h16p02.npwj.gretarttsi.hw2.shared.Domain.Item;
import se.kth.h16p02.npwj.gretarttsi.hw2.shared.Domain.SaleItem;
import se.kth.h16p02.npwj.gretarttsi.hw2.shared.Exceptions.RejectedException;

public interface MarketPlace extends Remote
{
    /**
     * Add item to wishlist
     * */
    void addItemToWishlist(Trader trader, Item item, BigDecimal maxPrice) throws RemoteException;

    /**
     * Register client (trader)
     * */
    boolean register(Trader trader) throws RemoteException, TraderAlreadyExistsException;

    /**
     * Deregister client (trader)
     * */
    boolean deregister(Trader trader) throws RemoteException;

    /**
     * Check if a trader is registered in the market place
     * */
    boolean checkIfRegistered(Trader trader) throws RemoteException;

    /**
     * Put item up for sale
     * */
    boolean sell(Trader trader, Item item) throws RemoteException, ItemAlreadyExistsException, TraderNotFoundException;

    /**
     * Buy item
     * */
    boolean buy(Trader trader, Item item) throws RemoteException, TraderNotFoundException, ItemNotFoundException, RejectedException;

    /**
     * Inspect what items are available on the marketplace
     * */
    ArrayList<SaleItem> inspectAvailableItems() throws RemoteException;
}
