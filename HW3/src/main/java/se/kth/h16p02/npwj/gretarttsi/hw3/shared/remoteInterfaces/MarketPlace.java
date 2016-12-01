package se.kth.h16p02.npwj.gretarttsi.hw3.shared.remoteInterfaces;

import se.kth.h16p02.npwj.gretarttsi.hw3.marketplace.exceptions.*;
import se.kth.h16p02.npwj.gretarttsi.hw3.shared.domain.Item;
import se.kth.h16p02.npwj.gretarttsi.hw3.shared.domain.SaleItem;
import se.kth.h16p02.npwj.gretarttsi.hw3.shared.domain.WishListItem;
import se.kth.h16p02.npwj.gretarttsi.hw3.shared.exceptions.InsufficientFundsException;
import se.kth.h16p02.npwj.gretarttsi.hw3.shared.exceptions.RejectedException;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface MarketPlace extends Remote
{
    /**
     * Add item to wishlist
     * */
    boolean addItemToWishlist(Trader trader, Item item) throws RemoteException, TraderNotFoundException;

    /**
     * Login to marketplace
     * */
    boolean login(Trader trader) throws RemoteException,TraderNotFoundException, PasswordNotFoundException;

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

    String ping () throws RemoteException;

}
