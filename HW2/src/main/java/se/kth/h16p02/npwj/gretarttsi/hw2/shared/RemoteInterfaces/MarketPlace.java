package se.kth.h16p02.npwj.gretarttsi.hw2.shared.RemoteInterfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import se.kth.h16p02.npwj.gretarttsi.hw2.bank.InsufficientFundsException;
import se.kth.h16p02.npwj.gretarttsi.hw2.marketplace.*;
import se.kth.h16p02.npwj.gretarttsi.hw2.shared.Domain.Item;
import se.kth.h16p02.npwj.gretarttsi.hw2.shared.Domain.SaleItem;
import se.kth.h16p02.npwj.gretarttsi.hw2.shared.Domain.WishListItem;
import se.kth.h16p02.npwj.gretarttsi.hw2.shared.Exceptions.RejectedException;

public interface MarketPlace extends Remote
{
    /**
     * Add item to wishlist
     * */
    boolean addItemToWishlist(Trader trader, Item item) throws RemoteException, TraderNotFoundException;

    /**
     * Find all trader´s wishes
     * */
    ArrayList<WishListItem> getTradersWishes(Trader trader) throws RemoteException;

    /**
     * Register client (trader)
     * */
    boolean register(Trader trader) throws RemoteException, TraderAlreadyExistsException, BankAccountNotFoundException;

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
    boolean buy(Trader trader, Item item) throws
            RemoteException,
            TraderNotFoundException,
            ItemNotFoundException,
            RejectedException,
            BankAccountNotFoundException,
            BuyException,
            InsufficientFundsException;

    /**
     * Inspect what items are available on the marketplace
     * */
    ArrayList<SaleItem> inspectAvailableItems() throws RemoteException;
}
