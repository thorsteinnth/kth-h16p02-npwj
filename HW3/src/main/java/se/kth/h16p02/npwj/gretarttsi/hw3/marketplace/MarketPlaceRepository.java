package se.kth.h16p02.npwj.gretarttsi.hw3.marketplace;

import se.kth.h16p02.npwj.gretarttsi.hw3.marketplace.database.MarketPlaceDAO;
import se.kth.h16p02.npwj.gretarttsi.hw3.marketplace.database.MarketplaceDBException;
import se.kth.h16p02.npwj.gretarttsi.hw3.marketplace.exceptions.ItemAlreadyExistsException;
import se.kth.h16p02.npwj.gretarttsi.hw3.marketplace.exceptions.ItemNotFoundException;
import se.kth.h16p02.npwj.gretarttsi.hw3.marketplace.exceptions.TraderAlreadyExistsException;
import se.kth.h16p02.npwj.gretarttsi.hw3.marketplace.exceptions.TraderNotFoundException;
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

    private ArrayList<Trader> traders;
    private ArrayList<SaleItem> saleItems;
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
        this.saleItems = new ArrayList<>();
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

    public boolean deregisterTrader(Trader trader)
    {
        this.traders.remove(trader);
        return true;
    }

    public boolean isTraderRegistered(Trader trader)
    {
        if (this.traders.contains(trader))
            return true;
        else
            return false;
    }

    private synchronized boolean isUsernameUnique(Trader trader) throws RemoteException, MarketplaceDBException
    {
        //TODO fara niður í grunn til þess að finna út hvort að usernameið sé unique

        boolean isUnique = true;

        // if trader exists then the password is not unique
        isUnique = !marketPlaceDAO.traderExists(trader.getUsername());

        /*
        for (Trader t : this.traders)
        {
            if (t.getUsername().equals(trader.getUsername()))
                isUnique = false;
        }
        */

        return isUnique;
    }

    //endregion

    //region Items

    public synchronized boolean addSaleItem(Trader trader, Item item) throws ItemAlreadyExistsException
    {
        if (!isItemUnique(item))
        {
           throw new ItemAlreadyExistsException("Item already exists in marketplace " + item.toString());
        }

        this.saleItems.add(new SaleItem(item,trader));

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
        WishListItem newWishListItem = new WishListItem(trader, item);

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

    public synchronized boolean removeTradersWishListItems(Trader trader)
    {
        ArrayList<WishListItem> wishListItems = getTradersWishes(trader);

        for(WishListItem wishListItem: wishListItems)
        {
            removeWishListItem(wishListItem);
        }
        return true;
    }

    public synchronized WishListItem findWishListItem(Trader trader, Item item) throws RemoteException
    {
        return findWishListItem(new WishListItem(trader, item));
    }

    public synchronized ArrayList<Trader> getTradersThatHaveItemInWishListForGreaterOrSamePrice(Item item)
    {
        ArrayList<Trader> tradersWithItemInWishlistForGreaterOrSamePrice = new ArrayList<>();
        ArrayList<WishListItem> wishListEntriesForItemName = getWishlistItemsByItemName(item);

        for (WishListItem wishListEntryForItem : wishListEntriesForItemName)
        {
            if (wishListEntryForItem.getItem().getPrice().compareTo(item.getPrice()) >= 0)
            {
                // Wish list entry price is less than or equal to the price of the item
                tradersWithItemInWishlistForGreaterOrSamePrice.add(wishListEntryForItem.getTrader());
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

    public synchronized ArrayList<WishListItem> getTradersWishes(Trader trader)
    {
        ArrayList<WishListItem> wishListItems = this.wishListItems
                .stream()
                .filter(wi -> wi.getTrader().equals(trader))
                .collect(Collectors.toCollection(ArrayList::new));

        return wishListItems;
    }

    //endregion
}
