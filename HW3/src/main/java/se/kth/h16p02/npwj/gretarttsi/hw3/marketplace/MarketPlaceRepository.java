package se.kth.h16p02.npwj.gretarttsi.hw3.marketplace;

import se.kth.h16p02.npwj.gretarttsi.hw3.marketplace.database.MarketPlaceDAO;
import se.kth.h16p02.npwj.gretarttsi.hw3.marketplace.database.MarketplaceDBException;
import se.kth.h16p02.npwj.gretarttsi.hw3.marketplace.exceptions.*;
import se.kth.h16p02.npwj.gretarttsi.hw3.shared.domain.Item;
import se.kth.h16p02.npwj.gretarttsi.hw3.shared.domain.SaleItem;
import se.kth.h16p02.npwj.gretarttsi.hw3.shared.domain.WishListItem;
import se.kth.h16p02.npwj.gretarttsi.hw3.shared.remoteInterfaces.Trader;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class MarketPlaceRepository
{
    private MarketPlaceDAO marketPlaceDAO;

    // TODO Rename this to loggedInUsers
    private ArrayList<Trader> traders;
    private ArrayList<WishListItem> wishListItems;

    public MarketPlaceRepository()
    {
        try
        {
            marketPlaceDAO = new MarketPlaceDAO();
        }
        catch (MarketplaceDBException e)
        {
            System.out.println(e.getMessage());
        }

        this.traders = new ArrayList<>();
        this.wishListItems = new ArrayList<>();
    }

    //region Traders

    public synchronized boolean registerTrader(Trader trader) throws TraderAlreadyExistsException
    {
        try
        {
            if (!isUsernameUnique(trader))
                throw new TraderAlreadyExistsException(
                        "Trader already exists with username: " + trader.getUsername()
                );
            try
            {
                marketPlaceDAO.createTrader(trader.getUsername(), trader.getPassword());
                this.traders.add(trader);
            }
            catch (MarketplaceDBException e)
            {
                System.out.println(e.getMessage());
                return false;
            }

            return true;
        }
        catch (RemoteException ex)
        {
            System.err.println(ex);
            return false;
        }
        catch (MarketplaceDBException e)
        {
            //System.err.println(e); // this is printed out in the MarketplaceDAO
            return false;
        }
    }

    public synchronized boolean loginTrader(Trader trader) throws
            RemoteException,
            TraderNotFoundException,
            PasswordNotFoundException {
        String password;
        try
        {
            password = marketPlaceDAO.GetTradersPassword(trader.getUsername());

            if(password.equals(trader.getPassword()))
            {
                // Add trader to TRADER cache to log him in
                this.traders.add(trader);
                return true;
            }
            else
            {
                throw new PasswordNotFoundException("Incorrect password");
            }
        }
        catch (MarketplaceDBException e)
        {
            System.out.println(e.getMessage());
            return false;
        }
    }


    public boolean logoutTrader(Trader trader)
    {
        this.traders.remove(trader);
        return true;
    }

    public boolean isTraderRegistered(Trader trader) throws RemoteException
    {
        try
        {
            return marketPlaceDAO.traderExists(trader.getUsername());
        }
        catch (MarketplaceDBException e)
        {
            System.out.println(e.getMessage());
            return false;
        }
    }

    private synchronized boolean isUsernameUnique(Trader trader) throws RemoteException, MarketplaceDBException
    {
        // if trader exists then the password is not unique
        return !marketPlaceDAO.traderExists(trader.getUsername());

    }


    // TODO Rename this to getLoggedInTraderByUsername?
    public Trader getTraderByUsername(String username) throws RemoteException
    {
        Trader foundTrader = null;

        for (Trader trader : this.traders)
        {
            if (trader.getUsername().equals(username))
                foundTrader = trader;
        }

        return foundTrader;
    }

    //endregion

    //region Items

    public synchronized boolean addSaleItem(Trader trader, Item item) throws ItemAlreadyExistsException, RemoteException
    {
        try
        {
            if (!isItemUnique(item))
            {
                throw new ItemAlreadyExistsException("Item already exists in marketplace " + item.toString());
            }

            this.marketPlaceDAO.createSaleItem(item.getName(), item.getPrice().intValue(), trader.getUsername(), null, false);
            return true;
        }
        catch (MarketplaceDBException ex)
        {
            System.err.println(ex);
            return false;
        }
    }

    public synchronized boolean markSaleItemAsSold(SaleItem saleItem, Trader buyer) throws RemoteException
    {
        try
        {
            this.marketPlaceDAO.setSaleItemSold(
                    saleItem.getItem().getName(),
                    saleItem.getItem().getPrice().intValue(),
                    true,
                    buyer.getUsername()
            );

            return true;
        }
        catch (MarketplaceDBException ex)
        {
            System.err.println(ex);
            return false;
        }
    }

    public synchronized ArrayList<SaleItem> getAllSaleItems()
    {
        try
        {
            return this.marketPlaceDAO.getAllSaleItems();
        }
        catch (MarketplaceDBException ex)
        {
            System.err.println(ex);
            return new ArrayList<>();
        }
    }

    public synchronized ArrayList<SaleItem> getSaleItemsBySeller(Trader seller) throws RemoteException
    {
        try
        {
            return this.marketPlaceDAO.getSaleItemsBySeller(seller.getUsername());
        }
        catch (MarketplaceDBException ex)
        {
            System.err.println(ex);
            return new ArrayList<>();
        }
    }

    public synchronized ArrayList<SaleItem> getSaleItemsByBuyer(Trader buyer) throws RemoteException
    {
        try
        {
            return this.marketPlaceDAO.getSaleItemsByBuyer(buyer.getUsername());
        }
        catch (MarketplaceDBException ex)
        {
            System.err.println(ex);
            return new ArrayList<>();
        }
    }

    public synchronized boolean itemExists(Item item)
    {
        return !isItemUnique(item);
    }

    public synchronized SaleItem findSaleItem(Item item) throws ItemNotFoundException
    {
        try
        {
            SaleItem foundItem = marketPlaceDAO.getSaleItem(item.getName(), item.getPrice().intValue());
            if (foundItem == null)
                throw new ItemNotFoundException("Could not find sale item for item: " + item.toDisplayString());

            return foundItem;
        }
        catch (MarketplaceDBException ex)
        {
            System.err.println(ex);
            throw new ItemNotFoundException("Could not find sale item for item: " + item.toDisplayString());
        }
    }

    private synchronized boolean isItemUnique(Item item)
    {
        try
        {
            // Check if item already present in database
            if (marketPlaceDAO.getSaleItem(item.getName(), item.getPrice().intValue()) == null)
                return true;
            else
                return false;
        }
        catch (MarketplaceDBException ex)
        {
            System.err.println(ex);
            return false;
        }
    }

    //endregion

    //region WishListItem

    public synchronized boolean addWishListItem(Trader trader, Item item) throws RemoteException
    {
        WishListItem newWishListItem = new WishListItem(trader.getUsername(), item);

        WishListItem containedWishListItem = findWishListItem(newWishListItem);

        if(containedWishListItem == null)
        {
            this.wishListItems.add(newWishListItem);
        }
        else
        {
            int index = this.wishListItems.indexOf(containedWishListItem);
            this.wishListItems.set(index, newWishListItem);
        }

        return true;
    }

    public synchronized boolean removeWishListItem(WishListItem wishListItem)
    {
        this.wishListItems.remove(wishListItem);
        return true;
    }

    public synchronized boolean removeTradersWishListItems(String username)
    {
        ArrayList<WishListItem> wishListItems = getTradersWishes(username);

        for(WishListItem wishListItem: wishListItems)
        {
            removeWishListItem(wishListItem);
        }
        return true;
    }

    public synchronized WishListItem findWishListItem(Trader trader, Item item) throws RemoteException
    {
        return findWishListItem(new WishListItem(trader.getUsername(), item));
    }

    public synchronized ArrayList<Trader> getTradersThatHaveItemInWishListForGreaterOrSamePrice(Item item) throws RemoteException
    {
        ArrayList<Trader> tradersWithItemInWishlistForGreaterOrSamePrice = new ArrayList<>();
        ArrayList<WishListItem> wishListEntriesForItemName = getWishlistItemsByItemName(item);

        for (WishListItem wishListEntryForItem : wishListEntriesForItemName)
        {
            if (wishListEntryForItem.getItem().getPrice().compareTo(item.getPrice()) >= 0)
            {
                // Wish list entry price is less than or equal to the price of the item
                // TODO Handle if trader is null, i.e. not logged in. Save notification to DB to deliver it later?
                Trader trader = getTraderByUsername(wishListEntryForItem.getUsername());
                if (trader != null)
                    tradersWithItemInWishlistForGreaterOrSamePrice.add(trader);
            }
        }

        return tradersWithItemInWishlistForGreaterOrSamePrice;
    }

    private synchronized WishListItem findWishListItem(WishListItem wishListItem) throws RemoteException
    {
        for(WishListItem listItem : this.wishListItems)
        {
            if (listItem.equalsWithoutPrice(wishListItem))
            {
                return listItem;
            }
        }

        return null;
    }

    private synchronized ArrayList<WishListItem> getWishlistItemsByItemName(Item item)
    {
        ArrayList<WishListItem> foundWishListItems = new ArrayList<>();

        for (WishListItem wlItem : this.wishListItems)
        {
            if (item.getName().equals(wlItem.getItem().getName()))
                foundWishListItems.add(wlItem);
        }

        return foundWishListItems;
    }

    public synchronized ArrayList<WishListItem> getTradersWishes(String username)
    {
        ArrayList<WishListItem> wishListItems = this.wishListItems
                .stream()
                .filter(wi -> wi.getUsername().equals(username))
                .collect(Collectors.toCollection(ArrayList::new));

        return wishListItems;
    }

    //endregion
}
