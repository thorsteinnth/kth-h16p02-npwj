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
import java.util.List;
import java.util.stream.Collectors;

public class MarketPlaceRepository
{
    private MarketPlaceDAO marketPlaceDAO;

    // TODO Rename this to loggedInUsers
    private ArrayList<Trader> traders;
    private ArrayList<SaleItem> saleItems;

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
        this.saleItems = new ArrayList<>();
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
        if (!isItemUnique(item))
        {
           throw new ItemAlreadyExistsException("Item already exists in marketplace " + item.toString());
        }

        this.saleItems.add(new SaleItem(item, trader.getUsername()));

        return true;
    }

    public synchronized boolean removeSaleItem(SaleItem saleItem)
    {
        this.saleItems.remove(saleItem);

        return true;
    }

    public synchronized ArrayList<SaleItem> getAllSaleItems()
    {
        return this.saleItems;
    }

    public synchronized boolean itemExists(Item item)
    {
        return !isItemUnique(item);
    }

    public synchronized SaleItem findSaleItem(Item item) throws ItemNotFoundException
    {
        List<SaleItem> foundSaleItems =
                this.saleItems
                        .stream()
                        .filter(si -> si.getItem().equals(item))
                        .collect(Collectors.toList());

        if (foundSaleItems.size() == 0)
            throw new ItemNotFoundException(item.toString());
        else if (foundSaleItems.size() > 1)
            throw new IllegalStateException("More than one item found: " + item);
        else
            return foundSaleItems.get(0);
    }

    private synchronized boolean isItemUnique(Item item)
    {
        for(SaleItem saleItem : this.saleItems)
        {
            if(item.equals(saleItem.getItem()))
            {
                return false;
            }
        }

        return true;
    }

    //endregion

    //region WishListItem

    public synchronized boolean addWishListItem(Trader trader, Item item) throws RemoteException
    {
        //TODO add the wishlistitem to database  call createWishlistItem

        // We add item to wishlist if an equal wishlist item is not found in the database
        // it is equal if n
        WishListItem wishListItem = new WishListItem(trader.getUsername(), item,false);

        ArrayList<WishListItem> tradersWishes =  getTradersWishes(trader.getUsername());

        for (WishListItem listItem:tradersWishes) {
            if (listItem.equalsWithoutBought(wishListItem))
            {
                return false;
            }
        }

        try
        {
            marketPlaceDAO.createWishlistItem(item.getName(), item.getPrice().intValue(), trader.getUsername(), false);
            return true;
        }
        catch (MarketplaceDBException e)
        {
            System.out.println(e.getMessage());
            return false;
        }
    }


    public synchronized ArrayList<WishListItem> getTradersWishes(String username)
    {
        //TODO test
        ArrayList<WishListItem> wishListItems;

        //Get all wishlist for this trader
        try
        {
            wishListItems = marketPlaceDAO.getWishlistItemsByUsername(username);
        }
        catch (MarketplaceDBException e)
        {
            System.out.println(e.getMessage());
            return null;
        }

        return wishListItems;
    }


    public synchronized ArrayList<WishListItem> getTradersWishHistory(String username)
    {
        ArrayList<WishListItem> wishListItems = getTradersWishes(username);

        for(WishListItem wishListItem: wishListItems)
        {
            if(!wishListItem.isBought())
                wishListItems.remove(wishListItem);
        }
        return wishListItems;
    }

    public synchronized ArrayList<WishListItem> getTradersWishesThatHaveNotBeenBought(String username)
    {
        ArrayList<WishListItem> wishListItems = getTradersWishes(username);

        for(WishListItem wishListItem: wishListItems)
        {
            if(wishListItem.isBought())
                wishListItems.remove(wishListItem);
        }
        return wishListItems;
    }


    public synchronized boolean markWishListItemBought(WishListItem wishListItem, boolean bought)
    {
        //TODO test

        try
        {
            //check if wishlistItemExists exists
            boolean itemExists = false;

            ArrayList<WishListItem> wishListItems = getTradersWishes(wishListItem.getUsername());

            for(WishListItem listItem: wishListItems)
            {
                if(listItem.equals(wishListItem))
                    itemExists = true;
            }

            if (itemExists)
            {
                marketPlaceDAO.setWishlistItemBought(
                        wishListItem.getItem().getName(),
                        wishListItem.getItem().getPrice().intValue(),
                        wishListItem.getUsername(),
                        bought
                );
            }
        }
        catch (MarketplaceDBException e)
        {
            System.out.println(e.getMessage());
            return false;
        }

        return true;
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


    private synchronized ArrayList<WishListItem> getWishlistItemsByItemName(Item item)
    {
        //Call to database
        try
        {
            ArrayList<WishListItem> wishListItems = marketPlaceDAO.getWishlistItemsByItemName(item.getName());
            // remove all entries that have been bought
            for(WishListItem wishListItem: wishListItems)
            {
                if(wishListItem.isBought())
                    wishListItems.remove(wishListItem);
            }

            return wishListItems;
        }
        catch (MarketplaceDBException e)
        {
            System.out.println(e.getMessage());
            return new ArrayList<WishListItem>();
        }
    }

    //endregion
}
